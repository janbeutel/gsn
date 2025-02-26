/**
* Global Sensor Networks (GSN) Source Code
* Copyright (c) 2006-2016, Ecole Polytechnique Federale de Lausanne (EPFL)
* Copyright (c) 2020-2023, University of Innsbruck
* 
* This file is part of GSN.
* 
* GSN is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* GSN is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with GSN.  If not, see <http://www.gnu.org/licenses/>.
* 
* File: src/ch/epfl/gsn/beans/StreamSource.java
*
* @author Mehdi Riahi
* @author gsn_devs
* @author Ali Salehi
* @author Timotee Maret
* @author Sofiane Sarni
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;

import org.slf4j.LoggerFactory;

import ch.epfl.gsn.Main;
import ch.epfl.gsn.beans.windowing.QueryRewriter;
import ch.epfl.gsn.beans.windowing.WindowType;
import ch.epfl.gsn.storage.SQLUtils;
import ch.epfl.gsn.utils.CaseInsensitiveComparator;
import ch.epfl.gsn.utils.GSNRuntimeException;
import ch.epfl.gsn.wrappers.AbstractWrapper;

import org.slf4j.Logger;

public class StreamSource implements Serializable {

  private static final long serialVersionUID = 5222853537667420098L;

  public static final String DEFAULT_QUERY = "select * from wrapper";

  private static final transient Logger logger = LoggerFactory.getLogger(StreamSource.class);

  private String alias;

  private float samplingRate;

  private String rawHistorySize = null;

  private String rawSlideValue = null;

  private int disconnectedBufferSize;

  private String sqlQuery;

  protected int uid;

  protected StringBuilder uidS;
 
  public static final AddressBean[] EMPTY_ADDRESS_BEAN = new AddressBean[] {};

  private AddressBean addressing[] = EMPTY_ADDRESS_BEAN;

  private transient AbstractWrapper wrapper;

  private InputStream inputStream;

  private AddressBean activeAddressBean; // To be used by the gui

  private transient QueryRewriter queryRewriter;
  private transient boolean isStorageCountBased = false;

  private WindowType windowingType = DEFAULT_WINDOW_TYPE;

  public static final long STORAGE_SIZE_NOT_SET = -1;
  public static final long DEFAULT_SLIDE_VALUE = 1;
  public static final WindowType DEFAULT_WINDOW_TYPE = WindowType.TUPLE_BASED_SLIDE_ON_EACH_TUPLE;

  private transient long parsedStorageSize = STORAGE_SIZE_NOT_SET;

  private transient long parsedSlideValue = DEFAULT_SLIDE_VALUE;

  private transient boolean isValidated = false;
  private transient boolean validationResult = false;

  private StringBuilder cachedSqlQuery = null;

  public String getRawHistorySize() {
    return rawHistorySize;
  }

  public StreamSource setRawHistorySize(String rawHistorySize) {
    this.rawHistorySize = rawHistorySize;
    isValidated = false;
    return this;
  }

  public StreamSource setRawSlideValue(String rawSlideValue) {
    this.rawSlideValue = rawSlideValue;
    isValidated = false;
    return this;
  }

  public StreamSource setAddressing(AddressBean[] addressing) {
    this.addressing = addressing;
    return this;
  }

  public StreamSource setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  public StreamSource setSqlQuery(String sqlQuery) {
    this.sqlQuery = sqlQuery;
    return this;
  }

  public AddressBean[] getAddressing() {
    if (addressing == null) {
      addressing = EMPTY_ADDRESS_BEAN;
    }

    return addressing;
  }

  /**
   * @return Returns the alias.
   */
  public CharSequence getAlias() {
    return alias.toLowerCase();
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  /**
   * @return Returns the bufferSize.
   */
  public int getDisconnectedBufferSize() {
    return disconnectedBufferSize;
  }

  public void setDisconnectedBufferSize(int disconnectedBufferSize) {
    this.disconnectedBufferSize = disconnectedBufferSize;
  }

  public float getSamplingRate() {
    return samplingRate;
  }

  public void setSamplingRate(float newRate) {
    if (cachedSqlQuery != null) {
      throw new GSNRuntimeException("Sampling rate can't be changed anymore !");
    }

    if (newRate >= 0 && newRate <= 1) {
      this.samplingRate = newRate;
    } else {
      throw new GSNRuntimeException("Invalid sampling rate is provided. Sampling rate is between 0 and 1.");
    }

  }

  /**
   * @return Returns the storageSize.
   */
  public String getStorageSize() {
    return this.rawHistorySize;
  }

  /**
   * @return the slide value
   */
  public String getSlideValue() {
    return rawSlideValue == null ? String.valueOf(DEFAULT_SLIDE_VALUE): rawSlideValue;
  }

  /**
   * @return Returns the sqlQuery.
   */
  public String getSqlQuery() {
    if (sqlQuery == null || sqlQuery.trim().length() == 0) {
      sqlQuery = DEFAULT_QUERY;
    }
    return sqlQuery;
  }

  public void setWrapper(AbstractWrapper wrapper) throws SQLException {
    if (!validate()) {
      throw new GSNRuntimeException("Can't set the wrapper when the stream source is invalid.");
    }

    this.wrapper = wrapper;
    this.activeAddressBean = wrapper.getActiveAddressBean();
    wrapper.addListener(this);
  }

  /**
   * @return Returns the activeSourceProducer.
   */
  public AbstractWrapper getWrapper() {
    if (wrapper == null) {
      throw new GSNRuntimeException("The wrapper for stream source is not set !.");
    }

    return wrapper;
  }

  /**
   * ;
   * Note that the validate method doesn't case if the wrapper variable or input
   * stream variable are set or not.
   * 
   * @return
   */
  public boolean validate() {
    if (isValidated) {
      return validationResult;
    }

    windowingType = DEFAULT_WINDOW_TYPE;
    isValidated = true;
    if (samplingRate <= 0) {
      logger
          .warn("The sampling rate is set to zero (or negative) which means no results. StreamSource = " + getAlias());
    } else if (samplingRate > 1) {
      samplingRate = 1;
      logger.warn("The provided sampling rate is greater than 1, resetting it to 1. StreamSource = " + getAlias());
    }
    if (getAddressing().length == 0) {
      logger.warn(
          "Validation failed because there is no addressing predicates provided for the stream source (the addressing part of the stream source is empty) stream source alias = "
              + getAlias());
      return validationResult = false;
    }
    if (this.rawHistorySize != null) {
      this.rawHistorySize = this.rawHistorySize.replace(" ", "").trim().toLowerCase();
      if (this.rawHistorySize.equalsIgnoreCase("")) {
        return validationResult = true;
      }
      final int second = 1000;
      final int minute = second * 60;
      final int hour = minute * 60;
      final int mIndex = this.rawHistorySize.indexOf("m");
      final int hIndex = this.rawHistorySize.indexOf("h");
      final int sIndex = this.rawHistorySize.indexOf("s");
      if (mIndex < 0 && hIndex < 0 && sIndex < 0) {
        try {
          this.parsedStorageSize = Long.parseLong(this.rawHistorySize);
          this.isStorageCountBased = true;
          windowingType = WindowType.TUPLE_BASED;
        } catch (final NumberFormatException e) {
          logger.error("The storage size, " + this.rawHistorySize + ", specified for the Stream Source : "
              + this.getAlias() + " is not valid.", e);
          return (validationResult = false);
        }
      } else {
        try {
          final StringBuilder shs = new StringBuilder(this.rawHistorySize);
          if (mIndex >= 0 && mIndex == shs.length() - 1) {
            this.parsedStorageSize = Long.parseLong(shs.deleteCharAt(mIndex).toString()) * minute;
          } else if (hIndex >= 0 && hIndex == shs.length() - 1) {
            this.parsedStorageSize = Long.parseLong(shs.deleteCharAt(hIndex).toString()) * hour;
          } else if (sIndex >= 0 && sIndex == shs.length() - 1) {
            this.parsedStorageSize = Long.parseLong(shs.deleteCharAt(sIndex).toString()) * second;
          } else {
            Long.parseLong("");
          }
          this.isStorageCountBased = false;
          windowingType = WindowType.TIME_BASED;
        } catch (NumberFormatException e) {
          logger.error("The storage size, " + rawHistorySize + ", specified for the Stream Source : " + this.getAlias()
              + " is not valid: " + e.getMessage());
          return (validationResult = false);
        }
      }

    }
    // Parsing slide value
    if (this.rawSlideValue == null) {
      // If slide value was not specified by the user, consider it as 1 tuple
      windowingType = (windowingType == WindowType.TUPLE_BASED) ? WindowType.TUPLE_BASED_SLIDE_ON_EACH_TUPLE
          : WindowType.TIME_BASED_SLIDE_ON_EACH_TUPLE;
      return validationResult = true;
    } else {
      this.rawSlideValue = this.rawSlideValue.replace(" ", "").trim().toLowerCase();
      // If slide value was not specified by the user, consider it as 1 tuple
      if (this.rawSlideValue.equalsIgnoreCase("")) {
        windowingType = (windowingType == WindowType.TUPLE_BASED) ? WindowType.TUPLE_BASED_SLIDE_ON_EACH_TUPLE
            : WindowType.TIME_BASED_SLIDE_ON_EACH_TUPLE;
        return validationResult = true;
      }
      final int second = 1000;
      final int minute = second * 60;
      final int hour = minute * 60;
      final int mIndex = this.rawSlideValue.indexOf("m");
      final int hIndex = this.rawSlideValue.indexOf("h");
      final int sIndex = this.rawSlideValue.indexOf("s");
      if (mIndex < 0 && hIndex < 0 && sIndex < 0) {
        try {
          this.parsedSlideValue = Long.parseLong(this.rawSlideValue);
          if (parsedSlideValue == 1) {// We consider this as a special case
            windowingType = (windowingType == WindowType.TIME_BASED) ? WindowType.TIME_BASED_SLIDE_ON_EACH_TUPLE
                : WindowType.TUPLE_BASED_SLIDE_ON_EACH_TUPLE;
          } else if (windowingType == WindowType.TIME_BASED) {
            windowingType = WindowType.TIME_BASED_WIN_TUPLE_BASED_SLIDE;
          }

        } catch (final NumberFormatException e) {
          logger.error("The slide value, " + rawSlideValue + ", specified for the Stream Source : " + getAlias()
              + " is not valid.", e);
          return (validationResult = false);
        }
      } else {
        try {
          final StringBuilder shs = new StringBuilder(this.rawSlideValue);
          if (mIndex >= 0 && mIndex == shs.length() - 1) {
            this.parsedSlideValue = Long.parseLong(shs.deleteCharAt(mIndex).toString()) * minute;
          } else if (hIndex >= 0 && hIndex == shs.length() - 1) {
            this.parsedSlideValue = Long.parseLong(shs.deleteCharAt(hIndex).toString()) * hour;
          } else if (sIndex >= 0 && sIndex == shs.length() - 1) {
            this.parsedSlideValue = Long.parseLong(shs.deleteCharAt(sIndex).toString()) * second;
          } else {
            Long.parseLong("");
          }
          if (windowingType == WindowType.TUPLE_BASED) {
            windowingType = WindowType.TUPLE_BASED_WIN_TIME_BASED_SLIDE;
          }

        } catch (NumberFormatException e) {
          logger.error("The slide value, " + rawSlideValue + ", specified for the Stream Source : " + getAlias()
              + " is not valid: " + e.getMessage());
          return (validationResult = false);
        }
      }

    }
    return validationResult = true;
  }

  public boolean isStorageCountBased() {
    validate();
    return this.isStorageCountBased;
  }

  public long getParsedStorageSize() {
    validate();
    return this.parsedStorageSize;
  }

  public WindowType getWindowingType() {
    validate();
    return windowingType;
  }

  public long getParsedSlideValue() {
    validate();
    return parsedSlideValue;
  }

  public boolean windowSlided() throws SQLException {
    if(logger.isDebugEnabled()){
      logger.debug("Data availble in the stream *" + getAlias() + "*");
    }
    
    return inputStream.executeQuery(getUIDStr());

  }

  public void setQueryRewriter(QueryRewriter rewriter) {
    this.queryRewriter = rewriter;
  }

  public QueryRewriter getQueryRewriter() {
    return queryRewriter;
  }

  public StringBuilder rewrite(String query) {
    if(queryRewriter == null){
      // TODO ??
      return null;
    } else {
      return queryRewriter.rewrite(query);
    }
  }

  public StringBuilder getUIDStr() {
    if (!validate()) {
      return null;
    }
    if (uidS == null) {
      uid = Main.getWindowStorage().tableNameGenerator();
      uidS = Main.getWindowStorage().tableNameGeneratorInString(uid);
    }
    return uidS;

  }

  public int hashCode() {
    return getUIDStr().hashCode();
  }

  // public Boolean dataAvailable ( ) throws SQLException {
  // if ( logger.isDebugEnabled( ) ) logger.debug( new StringBuilder( ).append(
  // "Data avialble in the stream *" ).append( getAlias( ) ).append( "*"
  // ).toString( ) );
  // return inputStream.dataAvailable( getUIDStr() );
  // }

  /**
   * This method gets a stream source specification and generates the appropriate
   * where clauses representing the stream source.
   * The method takes into account the start time, end time, sampling rate,
   * storage size (Both timed and count).
   * This method combines the conditions generated by the stream source
   * specification with the
   * actually conditions listed in the stream source query. Afterwades, the method
   * does the
   * renaming of the whole query. The result will be cased in this object for
   * later reuse.
   *
   * @return
   */
  public StringBuilder toSql() {
    if (cachedSqlQuery != null) {
      return cachedSqlQuery;
    }

    if (getWrapper() == null) {
      throw new GSNRuntimeException("Wrapper object is null, most probably a bug, please report it !");
    }
    if (!validate()) {
      throw new GSNRuntimeException("Validation of this object the stream source failed, please check the logs.");
    }

    CharSequence wrapperAlias = getWrapper().getDBAliasInStr();
    if (samplingRate == 0 || (isStorageCountBased && getParsedStorageSize() == 0)) {
      return cachedSqlQuery = new StringBuilder("select * from ").append(wrapperAlias).append(" where 1=0");
    }

    TreeMap<CharSequence, CharSequence> rewritingMapping = new TreeMap<CharSequence, CharSequence>(
        new CaseInsensitiveComparator());
    rewritingMapping.put("wrapper", wrapperAlias);
    StringBuilder toReturn = new StringBuilder(getSqlQuery());
    if (getSqlQuery().toLowerCase().indexOf(" where ") < 0) {
      toReturn.append(" where ");
    } else {
      toReturn.append(" and ");
    }

    // Applying the ** START AND END TIME ** for all types of windows based windows
    // toReturn.append(" wrapper.timed
    // >=").append(getStartDate().getTime()).append(" and timed
    // <=").append(getEndDate().getTime()).append(" and ");

    if (isStorageCountBased()) {
      if (Main.getWindowStorage().isH2() || Main.getWindowStorage().isMysqlDB()) {
        toReturn.append("timed >= (select distinct(timed) from ").append(wrapperAlias)
            .append(" order by timed desc limit 1 offset ").append(getParsedStorageSize() - 1).append(" )");
      } else if (Main.getWindowStorage().isSqlServer()) {
        toReturn.append("timed >= (select min(timed) from (select TOP ").append(getParsedStorageSize())
            .append(" timed from (select distinct(timed) from ").append(wrapperAlias)
            .append(") as x  order by timed desc ) as y )");
      }

    } else { // time based
      toReturn.append("(wrapper.timed >");
      if (Main.getWindowStorage().isH2()) {
        toReturn.append(" (NOW_MILLIS()");
      } else if (Main.getWindowStorage().isMysqlDB()) {
        toReturn.append(" (UNIX_TIMESTAMP()*1000");
      } else if (Main.getWindowStorage().isPostgres()) {
        toReturn.append(" (extract(epoch FROM now())*1000");
      } else if (Main.getWindowStorage().isSqlServer()) {
        // NOTE1 : The value retuend is in seconds (hence 1000)
        // NOTE2 : There is no time in the date for the epoch, maybe doesn't match with
        // the current system time, needs checking.
        toReturn.append(" (convert(bigint,datediff(second,'1/1/1970',current_timestamp))*1000 )");
      }
      toReturn.append(" - ").append(getParsedStorageSize()).append(" ) ) ");
    }
    if (samplingRate != 1) {
      toReturn.append(" and ( mod( timed , 100)< ").append(samplingRate * 100).append(")");
    }

    toReturn = new StringBuilder(SQLUtils.newRewrite(toReturn, rewritingMapping));
    // toReturn.append(" order by timed desc ");
    if(logger.isDebugEnabled()){
      logger.debug("The original query : " + getSqlQuery());
      logger.debug("The merged query : " + toReturn + " of the StreamSource " + getAlias() + " of the InputStream: "
          + inputStream.getInputStreamName());
    }
    
    return cachedSqlQuery = toReturn;
  }

  public StreamSource setInputStream(InputStream is) throws GSNRuntimeException {
    if (alias == null) {
      throw new NullPointerException("Alias can't be null!");
    }
    if (this.inputStream != null && is != this.inputStream) {
      throw new GSNRuntimeException("Can't reset the input stream variable !.");
    }
    this.inputStream = is;
    if (!validate()) {
      throw new GSNRuntimeException("You can't set the input stream on an invalid stream source. ");
    }
    return this;
  }

  /**
   * The wrapper in <code>StreamSource</code> is transient and is not serialized,
   * so
   * we use this method to specify the active address bean which is not accessible
   * via
   * the wrapper instance.
   * 
   * @return the active address bean
   */
  public AddressBean getActiveAddressBean() {
    return activeAddressBean;
  }

  public String toString() {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(" Stream Source object: ");
    toReturn.append(" Alias: ").append(alias);
    toReturn.append(" uidS: ").append(uidS);
    toReturn.append(" Active source: ").append(activeAddressBean);

    return toReturn.toString();
  }

}
