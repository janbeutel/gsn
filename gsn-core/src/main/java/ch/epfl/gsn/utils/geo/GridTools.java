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
* File: src/ch/epfl/gsn/utils/geo/GridTools.java
*
* @author Sofiane Sarni
* @author Milos Stojanovic
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.utils.geo;

import org.slf4j.LoggerFactory;

import ch.epfl.gsn.Main;
import ch.epfl.gsn.beans.DataTypes;

import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class GridTools {

    private static transient Logger logger = LoggerFactory.getLogger(GridTools.class);

    /**
     * Converts a byte array into a string representation of a deserialized 2D array
     * of Double values.
     *
     * @param bytes The byte array representing the serialized data.
     * @return A string representation of the deserialized data.
     */
    public static String deSerializeToString(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream in = null;

            in = new ObjectInputStream(bis);

            Double deserial[][] = new Double[0][];

            deserial = (Double[][]) in.readObject();
            in.close();
            
            if(logger.isDebugEnabled()){
				logger.debug("deserial.length" + deserial.length);
                logger.debug("deserial[0].length" + deserial[0].length);
			}

            for (int i = 0; i < deserial.length; i++) {

                for (int j = 0; j < deserial[0].length; j++) {
                    sb.append(deserial[i][j]).append(" ");
                }
                sb.append("\n");
            }

        } catch (IOException e) {
            logger.warn(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.warn(e.getMessage());
        }

        return sb.toString();
    }

    /**
     * Responsible for deserializing a byte array into a 2D array of Double values
     * and retrieving the valua at a specific cell position.
     *
     * @param bytes The byte array representing the serialized data.
     * @param xcell The x-coordinate of the desired cell.
     * @param ycell The y-coordinate of the desired cell.
     * @return The value at the specified cell position.
     */
    public static double deSerializeToCell(byte[] bytes, int xcell, int ycell) {

        double value = 0;

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream in = null;

            in = new ObjectInputStream(bis);

            Double deserial[][] = new Double[0][];

            deserial = (Double[][]) in.readObject();
            in.close();

            if(logger.isDebugEnabled()){
				logger.debug("deserial.length" + deserial.length);
                logger.debug("deserial[0].length" + deserial[0].length);
			}

            value = deserial[ycell][xcell];

        } catch (IOException e) {
            logger.warn(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.warn(e.getMessage());
        }

        return value;
    }

    /**
     * Responsible for deserializing a byte array into a 2D array of Double values
     * and returning a string representation of the deserialized data within
     * specified boundaries.
     *
     * @param bytes The byte array representing the serialized data.
     * @param xmin  The minimum x-coordinate of the desired data boundaries.
     * @param xmax  The maximum x-coordinate of the desired data boundaries.
     * @param ymin  The minimum y-coordinate of the desired data boundaries.
     * @param ymax  The maximum y-coordinate of the desired data boundaries.
     * @return A string representation of the deserialized data within the specified
     *         boundaries.
     */
    public static String deSerializeToStringWithBoundaries(byte[] bytes, int xmin, int xmax, int ymin, int ymax) {

        StringBuilder sb = new StringBuilder();

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream in = null;

            in = new ObjectInputStream(bis);

            Double deserial[][] = new Double[0][];

            deserial = (Double[][]) in.readObject();
            in.close();
            
            if(logger.isDebugEnabled()){
				logger.debug("deserial.length" + deserial.length);
                logger.debug("deserial[0].length" + deserial[0].length);
			}

            for (int i = ymin; i <= ymax; i++) {

                for (int j = xmin; j <= xmax; j++) {
                    sb.append(deserial[i][j]).append(" ");
                }
                sb.append("\n");
            }

        } catch (IOException e) {
            logger.warn(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.warn(e.getMessage());
        }

        return sb.toString();
    }

    /**
     * Deserializes a byte array into a 2D array of Double values.
     *
     * @param bytes The byte array representing the serialized data.
     * @return The deserialized 2D array of Double values.
     */
    public static Double[][] deSerialize(byte[] bytes) {

        Double deserial[][] = new Double[0][];

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream in = null;

            in = new ObjectInputStream(bis);

            deserial = (Double[][]) in.readObject();
            in.close();

            if(logger.isDebugEnabled()){
				logger.debug("deserial.length : " + deserial.length + ", deserial[0].length" + deserial[0].length);
			}

            for (int i = 0; i < deserial.length; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < deserial[0].length; j++) {
                    sb.append(deserial[i][j]).append(" ");
                }
                if(logger.isDebugEnabled()){
                    logger.debug(sb.toString());
                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }

        return deserial;
    }

    /**
     * Executes a SQL query on a database and returns the results as a formatted
     * string.
     *
     * @param query  The SQL query to be executed.
     * @param sensor The sensor for the database connection.
     * @return The results of the query as a formatted string.
     */
    public static String executeQueryForGridAsString(String query, String sensor) {

        Connection connection = null;
        StringBuilder sb = new StringBuilder();
        ResultSet results = null;

        try {
            connection = Main.getStorage(sensor).getConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            results = statement.executeQuery(query);
            ResultSetMetaData metaData; // Additional information about the results
            int numCols = 0, 
                numRows = 0; // How many rows and columns in the table
            metaData = results.getMetaData(); // Get metadata on them
            numCols = metaData.getColumnCount(); // How many columns?
            if(results.last()){// Move to last row
                numRows = results.getRow(); // How many rows?
            }
            

            String s;

            // headers
            sb.append("# Query: " + query + "\n");
            sb.append("# ");

            byte typ[] = new byte[numCols];
            String columnLabel[] = new String[numCols];

            for (int col = 0; col < numCols; col++) {
                columnLabel[col] = metaData.getColumnLabel(col + 1);
                typ[col] = Main.getDefaultStorage().convertLocalTypeToGSN(metaData.getColumnType(col + 1));
                if (typ[col] == -100) {
                    logger.error("The type can't be converted to GSN form - error description: column label is:"
                            + columnLabel[col] + ", query is: " + query);
                }
            }

            for (int row = 0; row < numRows; row++) {
                results.absolute(row + 1); // Go to the specified row
                for (int col = 0; col < numCols; col++) {
                    Object o = results.getObject(col + 1); // Get value of the column
                    if (o == null) {
                        s = "null";
                    } else {
                        s = o.toString();
                    }
                    if (typ[col] == DataTypes.BINARY) {
                        byte[] bin = (byte[]) o;
                        sb.append(GridTools.deSerializeToString(bin));
                    } else {
                        sb.append(columnLabel[col] + " " + s + "\n");
                    }
                }
                sb.append("\n");
            }
        } catch (SQLException e) {
            logger.warn("SQLException: " + e.getMessage());
            sb.append("ERROR in execution of query: " + e.getMessage());
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
            Main.getStorage(sensor).close(connection);
        }

        return sb.toString();
    }

    /**
     * Executes a SQL query on a database and returns the results as a map of
     * timestamps and values.
     * The query is executed for a specific cell identified by its x and y
     * coordinates.
     *
     * @param query  The SQL query to be executed.
     * @param xcell  The x-coordinate of the cell.
     * @param ycell  The y-coordinate of the cell.
     * @param sensor The sensor for the database connection.
     * @return A map of timestamps and values representing the result of the query.
     */
    public static Map<Long, Double> executeQueryForCell2TimeSeriesAsListOfDoubles(String query, int xcell, int ycell,
            String sensor) {

        Map<Long, Double> listOfDoubles = new HashMap<Long, Double>();
        Connection connection = null;
        StringBuilder sb = new StringBuilder();
        ResultSet results = null;

        try {
            connection = Main.getStorage(sensor).getConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            results = statement.executeQuery(query);
            ResultSetMetaData metaData; // Additional information about the results
            int numCols, numRows = 0; // How many rows and columns in the table
            metaData = results.getMetaData(); // Get metadata on them
            numCols = metaData.getColumnCount(); // How many columns?
            if(results.last()){// Move to last row
                numRows = results.getRow(); // How many rows?
            } 
            

            String s;

            byte typ[] = new byte[numCols];
            String columnLabel[] = new String[numCols];

            for (int col = 0; col < numCols; col++) {
                columnLabel[col] = metaData.getColumnLabel(col + 1);
                typ[col] = Main.getDefaultStorage().convertLocalTypeToGSN(metaData.getColumnType(col + 1));
                if (typ[col] == -100) {
                    logger.error("The type can't be converted to GSN form - error description: column label is: "
                            + columnLabel[col] + ", query is: " + query);
                }
            }

            Long timed = 0L;
            double value = 0.0;

            for (int row = 0; row < numRows; row++) {
                sb = new StringBuilder("");
                results.absolute(row + 1); // Go to the specified row
                for (int col = 0; col < numCols; col++) {
                    Object o = results.getObject(col + 1); // Get value of the column
                    if (o == null) {
                        s = "null";
                    } else {
                        s = o.toString();
                    }
                    if (columnLabel[col].equalsIgnoreCase("timed")) {
                        timed = Long.valueOf(s);
                        continue;
                    }
                    if (typ[col] == DataTypes.BINARY) {
                        byte[] bin = (byte[]) o;
                        value = GridTools.deSerializeToCell(bin, xcell, ycell);
                    }
                }

                listOfDoubles.put(timed, value);
            }

            // .add(sb.toString());
        } catch (SQLException e) {
            logger.warn("SQLException: " + e.getMessage());
            sb.append("ERROR in execution of query: " + e.getMessage());
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    logger.warn(e.getMessage(), e);
                }
            }

            Main.getStorage(sensor).close(connection);
        }

        return listOfDoubles;
    }

    /**
     * Executes a SQL query on a database and returns the results as a map of
     * timestamps and strings.
     *
     * @param query  the SQL query to execute
     * @param xmin   the minimum x coordinate of the subgrid
     * @param xmax   the maximum x coordinate of the subgrid
     * @param ymin   the minimum y coordinate of the subgrid
     * @param ymax   the maximum y coordinate of the subgrid
     * @param sensor the sensor for the database connection
     * @return a map of timestamps and strings representing the query results
     */
    public static Map<Long, String> executeQueryForSubGridAsListOfStrings(String query, int xmin, int xmax, int ymin,
            int ymax, String sensor) {

        Map<Long, String> listOfStrings = new HashMap<Long, String>();
        Connection connection = null;
        StringBuilder sb = new StringBuilder();
        ResultSet results = null;

        try {
            connection = Main.getStorage(sensor).getConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            results = statement.executeQuery(query);
            ResultSetMetaData metaData; // Additional information about the results
            int numCols, numRows = 0; // How many rows and columns in the table
            metaData = results.getMetaData(); // Get metadata on them
            numCols = metaData.getColumnCount(); // How many columns?
            if(results.last()){// Move to last row
                numRows = results.getRow(); // How many rows?
            }
            

            String s;

            byte typ[] = new byte[numCols];
            String columnLabel[] = new String[numCols];

            for (int col = 0; col < numCols; col++) {
                columnLabel[col] = metaData.getColumnLabel(col + 1);
                typ[col] = Main.getDefaultStorage().convertLocalTypeToGSN(metaData.getColumnType(col + 1));
                if (typ[col] == -100) {
                    logger.error("The type can't be converted to GSN form - error description: column label is:"
                            + columnLabel[col] + ", query is: " + query);
                }
            }

            Long timed = 0L;

            for (int row = 0; row < numRows; row++) {
                sb = new StringBuilder("");
                results.absolute(row + 1); // Go to the specified row
                for (int col = 0; col < numCols; col++) {
                    Object o = results.getObject(col + 1); // Get value of the column
                    if (o == null) {
                        s = "null";
                    } else {
                        s = o.toString();
                    }

                    if (columnLabel[col].equalsIgnoreCase("pk")) {
                        continue; // skip PK field
                    }

                    if (columnLabel[col].equalsIgnoreCase("timed")) {
                        timed = Long.valueOf(s);
                        continue;
                    }
                    if (typ[col] == DataTypes.BINARY) {
                        byte[] bin = (byte[]) o;
                        sb.append(GridTools.deSerializeToStringWithBoundaries(bin, xmin, xmax, ymin, ymax));
                    } else {
                        String fieldName = columnLabel[col];
                        String fieldValue = s;
                        if (fieldName.equalsIgnoreCase("ncols")) {
                            int nCols = xmax - xmin + 1;
                            fieldValue = Integer.toString(nCols);
                        } else if (fieldName.equalsIgnoreCase("nrows")) {
                            int nRows = ymax - ymin + 1;
                            fieldValue = Integer.toString(nRows);
                        }
                        sb.append(fieldName + " " + fieldValue + "\n");
                    }
                }
                sb.append("\n");
                listOfStrings.put(timed, sb.toString());
            }

            // .add(sb.toString());
        } catch (SQLException e) {
            logger.warn("SQLException: " + e.getMessage());
            sb.append("ERROR in execution of query: " + e.getMessage());
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
            Main.getStorage(sensor).close(connection);
        }

        return listOfStrings;
    }

    /**
     * Executes a SQL query on a database and returns the results as a map of
     * timestamps and strings.
     *
     * @param query  the SQL query to execute
     * @param sensor the sensor for the database connection
     * @return a map of timestamps and strings representing the query results
     */
    public static Map<Long, String> executeQueryForGridAsListOfStrings(String query, String sensor) {

        Map<Long, String> listOfStrings = new HashMap<Long, String>();
        Connection connection = null;
        StringBuilder sb = new StringBuilder();
        ResultSet results = null;

        try {
            connection = Main.getStorage(sensor).getConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            results = statement.executeQuery(query);
            ResultSetMetaData metaData; // Additional information about the results
            int numCols, numRows = 0; // How many rows and columns in the table
            metaData = results.getMetaData(); // Get metadata on them
            numCols = metaData.getColumnCount(); // How many columns?
            if(results.last()){// Move to last row
                numRows = results.getRow(); // How many rows?
            } 

            String s;

            byte typ[] = new byte[numCols];
            String columnLabel[] = new String[numCols];

            for (int col = 0; col < numCols; col++) {
                columnLabel[col] = metaData.getColumnLabel(col + 1);
                typ[col] = Main.getDefaultStorage().convertLocalTypeToGSN(metaData.getColumnType(col + 1));
                if (typ[col] == -100) {
                    logger.error("The type can't be converted to GSN form - error description:  column label is:"
                            + columnLabel[col] + ", query is: " + query);
                }
            }

            Long timed = 0L;

            for (int row = 0; row < numRows; row++) {
                sb = new StringBuilder("");
                results.absolute(row + 1); // Go to the specified row
                for (int col = 0; col < numCols; col++) {
                    Object o = results.getObject(col + 1); // Get value of the column
                    if (o == null) {
                        s = "null";
                    } else {
                        s = o.toString();
                    }
                    if (columnLabel[col].equalsIgnoreCase("pk")) {
                        continue; // skip PK field
                    }
                    if (columnLabel[col].equalsIgnoreCase("timed")) {
                        timed = Long.valueOf(s);
                        continue;
                    }
                    if (typ[col] == DataTypes.BINARY) {
                        byte[] bin = (byte[]) o;
                        sb.append(GridTools.deSerializeToString(bin));
                    } else {
                        String fieldName = columnLabel[col];
                        String fieldValue = s;

                        sb.append(fieldName + " " + fieldValue + "\n");
                    }
                }
                sb.append("\n");
                listOfStrings.put(timed, sb.toString());
            }

            // .add(sb.toString());
        } catch (SQLException e) {
            sb.append("ERROR in execution of query: " + e.getMessage());
            logger.warn("SQLException: " + e.getMessage());
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    logger.warn(e.getMessage(), e);
                }
            }

            Main.getStorage(sensor).close(connection);
        }

        return listOfStrings;
    }
}

