/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'DozerAEStatisticsOldMsg'
 * message type.
 */

 package ch.epfl.gsn.wrappers.backlog.plugins.tinyos2x;

public class DozerAEStatisticsOldMsg extends DataHeaderMsg {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 17;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 196;

    /** Create a new DozerAEStatisticsOldMsg of size 17. */
    public DozerAEStatisticsOldMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new DozerAEStatisticsOldMsg of the given data_length. */
    public DozerAEStatisticsOldMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerAEStatisticsOldMsg with the given data_length
     * and base offset.
     */
    public DozerAEStatisticsOldMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerAEStatisticsOldMsg using the given byte array
     * as backing store.
     */
    public DozerAEStatisticsOldMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerAEStatisticsOldMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public DozerAEStatisticsOldMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerAEStatisticsOldMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public DozerAEStatisticsOldMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerAEStatisticsOldMsg embedded in the given message
     * at the given base offset.
     */
    public DozerAEStatisticsOldMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerAEStatisticsOldMsg embedded in the given message
     * at the given base offset and length.
     */
    public DozerAEStatisticsOldMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <DozerAEStatisticsOldMsg> \n";
      try {
        s += "  [header.seqNr=0x"+Long.toHexString(get_header_seqNr())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [header.originatorID=0x"+Long.toHexString(get_header_originatorID())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [header.aTime.low=0x"+Long.toHexString(get_header_aTime_low())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [header.aTime.high=0x"+Long.toHexString(get_header_aTime_high())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.measured=0x"+Long.toHexString(get_payload_measured())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.parametrized=0x"+Long.toHexString(get_payload_parametrized())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.stored=0x"+Long.toHexString(get_payload_stored())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.bufferunderflows=0x"+Long.toHexString(get_payload_bufferunderflows())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.noisefloor_ch1=0x"+Long.toHexString(get_payload_noisefloor_ch1())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.noisefloor_ch2=0x"+Long.toHexString(get_payload_noisefloor_ch2())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: header.seqNr
    //   Field type: int
    //   Offset (bits): 0
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'header.seqNr' is signed (false).
     */
    public static boolean isSigned_header_seqNr() {
        return false;
    }

    /**
     * Return whether the field 'header.seqNr' is an array (false).
     */
    public static boolean isArray_header_seqNr() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'header.seqNr'
     */
    public static int offset_header_seqNr() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'header.seqNr'
     */
    public static int offsetBits_header_seqNr() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'header.seqNr'
     */
    public int get_header_seqNr() {
        return (int)getUIntBEElement(offsetBits_header_seqNr(), 16);
    }

    /**
     * Set the value of the field 'header.seqNr'
     */
    public void set_header_seqNr(int value) {
        setUIntBEElement(offsetBits_header_seqNr(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'header.seqNr'
     */
    public static int size_header_seqNr() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'header.seqNr'
     */
    public static int sizeBits_header_seqNr() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: header.originatorID
    //   Field type: int
    //   Offset (bits): 16
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'header.originatorID' is signed (false).
     */
    public static boolean isSigned_header_originatorID() {
        return false;
    }

    /**
     * Return whether the field 'header.originatorID' is an array (false).
     */
    public static boolean isArray_header_originatorID() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'header.originatorID'
     */
    public static int offset_header_originatorID() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'header.originatorID'
     */
    public static int offsetBits_header_originatorID() {
        return 16;
    }

    /**
     * Return the value (as a int) of the field 'header.originatorID'
     */
    public int get_header_originatorID() {
        return (int)getUIntBEElement(offsetBits_header_originatorID(), 16);
    }

    /**
     * Set the value of the field 'header.originatorID'
     */
    public void set_header_originatorID(int value) {
        setUIntBEElement(offsetBits_header_originatorID(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'header.originatorID'
     */
    public static int size_header_originatorID() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'header.originatorID'
     */
    public static int sizeBits_header_originatorID() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: header.aTime.low
    //   Field type: int
    //   Offset (bits): 32
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'header.aTime.low' is signed (false).
     */
    public static boolean isSigned_header_aTime_low() {
        return false;
    }

    /**
     * Return whether the field 'header.aTime.low' is an array (false).
     */
    public static boolean isArray_header_aTime_low() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'header.aTime.low'
     */
    public static int offset_header_aTime_low() {
        return (32 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'header.aTime.low'
     */
    public static int offsetBits_header_aTime_low() {
        return 32;
    }

    /**
     * Return the value (as a int) of the field 'header.aTime.low'
     */
    public int get_header_aTime_low() {
        return (int)getUIntBEElement(offsetBits_header_aTime_low(), 16);
    }

    /**
     * Set the value of the field 'header.aTime.low'
     */
    public void set_header_aTime_low(int value) {
        setUIntBEElement(offsetBits_header_aTime_low(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'header.aTime.low'
     */
    public static int size_header_aTime_low() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'header.aTime.low'
     */
    public static int sizeBits_header_aTime_low() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: header.aTime.high
    //   Field type: short
    //   Offset (bits): 48
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'header.aTime.high' is signed (false).
     */
    public static boolean isSigned_header_aTime_high() {
        return false;
    }

    /**
     * Return whether the field 'header.aTime.high' is an array (false).
     */
    public static boolean isArray_header_aTime_high() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'header.aTime.high'
     */
    public static int offset_header_aTime_high() {
        return (48 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'header.aTime.high'
     */
    public static int offsetBits_header_aTime_high() {
        return 48;
    }

    /**
     * Return the value (as a short) of the field 'header.aTime.high'
     */
    public short get_header_aTime_high() {
        return (short)getUIntBEElement(offsetBits_header_aTime_high(), 8);
    }

    /**
     * Set the value of the field 'header.aTime.high'
     */
    public void set_header_aTime_high(short value) {
        setUIntBEElement(offsetBits_header_aTime_high(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'header.aTime.high'
     */
    public static int size_header_aTime_high() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'header.aTime.high'
     */
    public static int sizeBits_header_aTime_high() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.measured
    //   Field type: int
    //   Offset (bits): 56
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.measured' is signed (false).
     */
    public static boolean isSigned_payload_measured() {
        return false;
    }

    /**
     * Return whether the field 'payload.measured' is an array (false).
     */
    public static boolean isArray_payload_measured() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.measured'
     */
    public static int offset_payload_measured() {
        return (56 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.measured'
     */
    public static int offsetBits_payload_measured() {
        return 56;
    }

    /**
     * Return the value (as a int) of the field 'payload.measured'
     */
    public int get_payload_measured() {
        return (int)getUIntBEElement(offsetBits_payload_measured(), 16);
    }

    /**
     * Set the value of the field 'payload.measured'
     */
    public void set_payload_measured(int value) {
        setUIntBEElement(offsetBits_payload_measured(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.measured'
     */
    public static int size_payload_measured() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.measured'
     */
    public static int sizeBits_payload_measured() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.parametrized
    //   Field type: int
    //   Offset (bits): 72
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.parametrized' is signed (false).
     */
    public static boolean isSigned_payload_parametrized() {
        return false;
    }

    /**
     * Return whether the field 'payload.parametrized' is an array (false).
     */
    public static boolean isArray_payload_parametrized() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.parametrized'
     */
    public static int offset_payload_parametrized() {
        return (72 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.parametrized'
     */
    public static int offsetBits_payload_parametrized() {
        return 72;
    }

    /**
     * Return the value (as a int) of the field 'payload.parametrized'
     */
    public int get_payload_parametrized() {
        return (int)getUIntBEElement(offsetBits_payload_parametrized(), 16);
    }

    /**
     * Set the value of the field 'payload.parametrized'
     */
    public void set_payload_parametrized(int value) {
        setUIntBEElement(offsetBits_payload_parametrized(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.parametrized'
     */
    public static int size_payload_parametrized() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.parametrized'
     */
    public static int sizeBits_payload_parametrized() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.stored
    //   Field type: int
    //   Offset (bits): 88
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.stored' is signed (false).
     */
    public static boolean isSigned_payload_stored() {
        return false;
    }

    /**
     * Return whether the field 'payload.stored' is an array (false).
     */
    public static boolean isArray_payload_stored() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.stored'
     */
    public static int offset_payload_stored() {
        return (88 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.stored'
     */
    public static int offsetBits_payload_stored() {
        return 88;
    }

    /**
     * Return the value (as a int) of the field 'payload.stored'
     */
    public int get_payload_stored() {
        return (int)getUIntBEElement(offsetBits_payload_stored(), 16);
    }

    /**
     * Set the value of the field 'payload.stored'
     */
    public void set_payload_stored(int value) {
        setUIntBEElement(offsetBits_payload_stored(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.stored'
     */
    public static int size_payload_stored() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.stored'
     */
    public static int sizeBits_payload_stored() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.bufferunderflows
    //   Field type: int
    //   Offset (bits): 104
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.bufferunderflows' is signed (false).
     */
    public static boolean isSigned_payload_bufferunderflows() {
        return false;
    }

    /**
     * Return whether the field 'payload.bufferunderflows' is an array (false).
     */
    public static boolean isArray_payload_bufferunderflows() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.bufferunderflows'
     */
    public static int offset_payload_bufferunderflows() {
        return (104 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.bufferunderflows'
     */
    public static int offsetBits_payload_bufferunderflows() {
        return 104;
    }

    /**
     * Return the value (as a int) of the field 'payload.bufferunderflows'
     */
    public int get_payload_bufferunderflows() {
        return (int)getUIntBEElement(offsetBits_payload_bufferunderflows(), 16);
    }

    /**
     * Set the value of the field 'payload.bufferunderflows'
     */
    public void set_payload_bufferunderflows(int value) {
        setUIntBEElement(offsetBits_payload_bufferunderflows(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.bufferunderflows'
     */
    public static int size_payload_bufferunderflows() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.bufferunderflows'
     */
    public static int sizeBits_payload_bufferunderflows() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.noisefloor_ch1
    //   Field type: short
    //   Offset (bits): 120
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.noisefloor_ch1' is signed (false).
     */
    public static boolean isSigned_payload_noisefloor_ch1() {
        return false;
    }

    /**
     * Return whether the field 'payload.noisefloor_ch1' is an array (false).
     */
    public static boolean isArray_payload_noisefloor_ch1() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.noisefloor_ch1'
     */
    public static int offset_payload_noisefloor_ch1() {
        return (120 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.noisefloor_ch1'
     */
    public static int offsetBits_payload_noisefloor_ch1() {
        return 120;
    }

    /**
     * Return the value (as a short) of the field 'payload.noisefloor_ch1'
     */
    public short get_payload_noisefloor_ch1() {
        return (short)getUIntBEElement(offsetBits_payload_noisefloor_ch1(), 8);
    }

    /**
     * Set the value of the field 'payload.noisefloor_ch1'
     */
    public void set_payload_noisefloor_ch1(short value) {
        setUIntBEElement(offsetBits_payload_noisefloor_ch1(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.noisefloor_ch1'
     */
    public static int size_payload_noisefloor_ch1() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.noisefloor_ch1'
     */
    public static int sizeBits_payload_noisefloor_ch1() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.noisefloor_ch2
    //   Field type: short
    //   Offset (bits): 128
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.noisefloor_ch2' is signed (false).
     */
    public static boolean isSigned_payload_noisefloor_ch2() {
        return false;
    }

    /**
     * Return whether the field 'payload.noisefloor_ch2' is an array (false).
     */
    public static boolean isArray_payload_noisefloor_ch2() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.noisefloor_ch2'
     */
    public static int offset_payload_noisefloor_ch2() {
        return (128 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.noisefloor_ch2'
     */
    public static int offsetBits_payload_noisefloor_ch2() {
        return 128;
    }

    /**
     * Return the value (as a short) of the field 'payload.noisefloor_ch2'
     */
    public short get_payload_noisefloor_ch2() {
        return (short)getUIntBEElement(offsetBits_payload_noisefloor_ch2(), 8);
    }

    /**
     * Set the value of the field 'payload.noisefloor_ch2'
     */
    public void set_payload_noisefloor_ch2(short value) {
        setUIntBEElement(offsetBits_payload_noisefloor_ch2(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.noisefloor_ch2'
     */
    public static int size_payload_noisefloor_ch2() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.noisefloor_ch2'
     */
    public static int sizeBits_payload_noisefloor_ch2() {
        return 8;
    }

}
