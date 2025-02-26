/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'DozerWgpsStatusMsg'
 * message type.
 */

 package ch.epfl.gsn.wrappers.backlog.plugins.tinyos2x;

public class DozerWgpsStatusMsg extends DataHeaderMsg {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 28;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 188;

    /** Create a new DozerWgpsStatusMsg of size 28. */
    public DozerWgpsStatusMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new DozerWgpsStatusMsg of the given data_length. */
    public DozerWgpsStatusMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerWgpsStatusMsg with the given data_length
     * and base offset.
     */
    public DozerWgpsStatusMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerWgpsStatusMsg using the given byte array
     * as backing store.
     */
    public DozerWgpsStatusMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerWgpsStatusMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public DozerWgpsStatusMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerWgpsStatusMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public DozerWgpsStatusMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerWgpsStatusMsg embedded in the given message
     * at the given base offset.
     */
    public DozerWgpsStatusMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerWgpsStatusMsg embedded in the given message
     * at the given base offset and length.
     */
    public DozerWgpsStatusMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <DozerWgpsStatusMsg> \n";
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
        s += "  [payload.sample.valid=0x"+Long.toHexString(get_payload_sample_valid())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.sample.no=0x"+Long.toHexString(get_payload_sample_no())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.gps_voltage=0x"+Long.toHexString(get_payload_gps_voltage())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.gps_current=0x"+Long.toHexString(get_payload_gps_current())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.sam7_current=0x"+Long.toHexString(get_payload_sam7_current())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.tn_per_current=0x"+Long.toHexString(get_payload_tn_per_current())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.sam7_power=0x"+Long.toHexString(get_payload_sam7_power())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.sam7_valid=0x"+Long.toHexString(get_payload_sam7_valid())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.inclinometer_x=0x"+Long.toHexString(get_payload_inclinometer_x())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.inclinometer_y=0x"+Long.toHexString(get_payload_inclinometer_y())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.sam7_unixtime=0x"+Long.toHexString(get_payload_sam7_unixtime())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.gps_sv_queue=0x"+Long.toHexString(get_payload_gps_sv_queue())+"]\n";
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
    // Accessor methods for field: payload.sample.valid
    //   Field type: byte
    //   Offset (bits): 56
    //   Size (bits): 1
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.sample.valid' is signed (false).
     */
    public static boolean isSigned_payload_sample_valid() {
        return false;
    }

    /**
     * Return whether the field 'payload.sample.valid' is an array (false).
     */
    public static boolean isArray_payload_sample_valid() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.sample.valid'
     */
    public static int offset_payload_sample_valid() {
        return (56 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.sample.valid'
     */
    public static int offsetBits_payload_sample_valid() {
        return 56;
    }

    /**
     * Return the value (as a byte) of the field 'payload.sample.valid'
     */
    public byte get_payload_sample_valid() {
        return (byte)getUIntBEElement(offsetBits_payload_sample_valid(), 1);
    }

    /**
     * Set the value of the field 'payload.sample.valid'
     */
    public void set_payload_sample_valid(byte value) {
        setUIntBEElement(offsetBits_payload_sample_valid(), 1, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.sample.valid'
     * WARNING: This field is not an even-sized number of bytes (1 bits).
     */
    public static int size_payload_sample_valid() {
        return (1 / 8) + 1;
    }

    /**
     * Return the size, in bits, of the field 'payload.sample.valid'
     */
    public static int sizeBits_payload_sample_valid() {
        return 1;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.sample.no
    //   Field type: short
    //   Offset (bits): 57
    //   Size (bits): 15
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.sample.no' is signed (false).
     */
    public static boolean isSigned_payload_sample_no() {
        return false;
    }

    /**
     * Return whether the field 'payload.sample.no' is an array (false).
     */
    public static boolean isArray_payload_sample_no() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.sample.no'
     * WARNING: This field is not byte-aligned (bit offset 57).
     */
    public static int offset_payload_sample_no() {
        return (57 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.sample.no'
     */
    public static int offsetBits_payload_sample_no() {
        return 57;
    }

    /**
     * Return the value (as a short) of the field 'payload.sample.no'
     */
    public short get_payload_sample_no() {
        return (short)getUIntBEElement(offsetBits_payload_sample_no(), 15);
    }

    /**
     * Set the value of the field 'payload.sample.no'
     */
    public void set_payload_sample_no(short value) {
        setUIntBEElement(offsetBits_payload_sample_no(), 15, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.sample.no'
     * WARNING: This field is not an even-sized number of bytes (15 bits).
     */
    public static int size_payload_sample_no() {
        return (15 / 8) + 1;
    }

    /**
     * Return the size, in bits, of the field 'payload.sample.no'
     */
    public static int sizeBits_payload_sample_no() {
        return 15;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.gps_voltage
    //   Field type: int
    //   Offset (bits): 72
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.gps_voltage' is signed (false).
     */
    public static boolean isSigned_payload_gps_voltage() {
        return false;
    }

    /**
     * Return whether the field 'payload.gps_voltage' is an array (false).
     */
    public static boolean isArray_payload_gps_voltage() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.gps_voltage'
     */
    public static int offset_payload_gps_voltage() {
        return (72 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.gps_voltage'
     */
    public static int offsetBits_payload_gps_voltage() {
        return 72;
    }

    /**
     * Return the value (as a int) of the field 'payload.gps_voltage'
     */
    public int get_payload_gps_voltage() {
        return (int)getUIntBEElement(offsetBits_payload_gps_voltage(), 16);
    }

    /**
     * Set the value of the field 'payload.gps_voltage'
     */
    public void set_payload_gps_voltage(int value) {
        setUIntBEElement(offsetBits_payload_gps_voltage(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.gps_voltage'
     */
    public static int size_payload_gps_voltage() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.gps_voltage'
     */
    public static int sizeBits_payload_gps_voltage() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.gps_current
    //   Field type: int
    //   Offset (bits): 88
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.gps_current' is signed (false).
     */
    public static boolean isSigned_payload_gps_current() {
        return false;
    }

    /**
     * Return whether the field 'payload.gps_current' is an array (false).
     */
    public static boolean isArray_payload_gps_current() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.gps_current'
     */
    public static int offset_payload_gps_current() {
        return (88 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.gps_current'
     */
    public static int offsetBits_payload_gps_current() {
        return 88;
    }

    /**
     * Return the value (as a int) of the field 'payload.gps_current'
     */
    public int get_payload_gps_current() {
        return (int)getUIntBEElement(offsetBits_payload_gps_current(), 16);
    }

    /**
     * Set the value of the field 'payload.gps_current'
     */
    public void set_payload_gps_current(int value) {
        setUIntBEElement(offsetBits_payload_gps_current(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.gps_current'
     */
    public static int size_payload_gps_current() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.gps_current'
     */
    public static int sizeBits_payload_gps_current() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.sam7_current
    //   Field type: int
    //   Offset (bits): 104
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.sam7_current' is signed (false).
     */
    public static boolean isSigned_payload_sam7_current() {
        return false;
    }

    /**
     * Return whether the field 'payload.sam7_current' is an array (false).
     */
    public static boolean isArray_payload_sam7_current() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.sam7_current'
     */
    public static int offset_payload_sam7_current() {
        return (104 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.sam7_current'
     */
    public static int offsetBits_payload_sam7_current() {
        return 104;
    }

    /**
     * Return the value (as a int) of the field 'payload.sam7_current'
     */
    public int get_payload_sam7_current() {
        return (int)getUIntBEElement(offsetBits_payload_sam7_current(), 16);
    }

    /**
     * Set the value of the field 'payload.sam7_current'
     */
    public void set_payload_sam7_current(int value) {
        setUIntBEElement(offsetBits_payload_sam7_current(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.sam7_current'
     */
    public static int size_payload_sam7_current() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.sam7_current'
     */
    public static int sizeBits_payload_sam7_current() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.tn_per_current
    //   Field type: int
    //   Offset (bits): 120
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.tn_per_current' is signed (false).
     */
    public static boolean isSigned_payload_tn_per_current() {
        return false;
    }

    /**
     * Return whether the field 'payload.tn_per_current' is an array (false).
     */
    public static boolean isArray_payload_tn_per_current() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.tn_per_current'
     */
    public static int offset_payload_tn_per_current() {
        return (120 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.tn_per_current'
     */
    public static int offsetBits_payload_tn_per_current() {
        return 120;
    }

    /**
     * Return the value (as a int) of the field 'payload.tn_per_current'
     */
    public int get_payload_tn_per_current() {
        return (int)getUIntBEElement(offsetBits_payload_tn_per_current(), 16);
    }

    /**
     * Set the value of the field 'payload.tn_per_current'
     */
    public void set_payload_tn_per_current(int value) {
        setUIntBEElement(offsetBits_payload_tn_per_current(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.tn_per_current'
     */
    public static int size_payload_tn_per_current() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.tn_per_current'
     */
    public static int sizeBits_payload_tn_per_current() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.sam7_power
    //   Field type: byte
    //   Offset (bits): 136
    //   Size (bits): 1
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.sam7_power' is signed (false).
     */
    public static boolean isSigned_payload_sam7_power() {
        return false;
    }

    /**
     * Return whether the field 'payload.sam7_power' is an array (false).
     */
    public static boolean isArray_payload_sam7_power() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.sam7_power'
     */
    public static int offset_payload_sam7_power() {
        return (136 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.sam7_power'
     */
    public static int offsetBits_payload_sam7_power() {
        return 136;
    }

    /**
     * Return the value (as a byte) of the field 'payload.sam7_power'
     */
    public byte get_payload_sam7_power() {
        return (byte)getUIntBEElement(offsetBits_payload_sam7_power(), 1);
    }

    /**
     * Set the value of the field 'payload.sam7_power'
     */
    public void set_payload_sam7_power(byte value) {
        setUIntBEElement(offsetBits_payload_sam7_power(), 1, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.sam7_power'
     * WARNING: This field is not an even-sized number of bytes (1 bits).
     */
    public static int size_payload_sam7_power() {
        return (1 / 8) + 1;
    }

    /**
     * Return the size, in bits, of the field 'payload.sam7_power'
     */
    public static int sizeBits_payload_sam7_power() {
        return 1;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.sam7_valid
    //   Field type: byte
    //   Offset (bits): 137
    //   Size (bits): 1
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.sam7_valid' is signed (false).
     */
    public static boolean isSigned_payload_sam7_valid() {
        return false;
    }

    /**
     * Return whether the field 'payload.sam7_valid' is an array (false).
     */
    public static boolean isArray_payload_sam7_valid() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.sam7_valid'
     * WARNING: This field is not byte-aligned (bit offset 137).
     */
    public static int offset_payload_sam7_valid() {
        return (137 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.sam7_valid'
     */
    public static int offsetBits_payload_sam7_valid() {
        return 137;
    }

    /**
     * Return the value (as a byte) of the field 'payload.sam7_valid'
     */
    public byte get_payload_sam7_valid() {
        return (byte)getUIntBEElement(offsetBits_payload_sam7_valid(), 1);
    }

    /**
     * Set the value of the field 'payload.sam7_valid'
     */
    public void set_payload_sam7_valid(byte value) {
        setUIntBEElement(offsetBits_payload_sam7_valid(), 1, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.sam7_valid'
     * WARNING: This field is not an even-sized number of bytes (1 bits).
     */
    public static int size_payload_sam7_valid() {
        return (1 / 8) + 1;
    }

    /**
     * Return the size, in bits, of the field 'payload.sam7_valid'
     */
    public static int sizeBits_payload_sam7_valid() {
        return 1;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.inclinometer_x
    //   Field type: short
    //   Offset (bits): 144
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.inclinometer_x' is signed (false).
     */
    public static boolean isSigned_payload_inclinometer_x() {
        return false;
    }

    /**
     * Return whether the field 'payload.inclinometer_x' is an array (false).
     */
    public static boolean isArray_payload_inclinometer_x() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.inclinometer_x'
     */
    public static int offset_payload_inclinometer_x() {
        return (144 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.inclinometer_x'
     */
    public static int offsetBits_payload_inclinometer_x() {
        return 144;
    }

    /**
     * Return the value (as a short) of the field 'payload.inclinometer_x'
     */
    public short get_payload_inclinometer_x() {
        return (short)getSIntBEElement(offsetBits_payload_inclinometer_x(), 16);
    }

    /**
     * Set the value of the field 'payload.inclinometer_x'
     */
    public void set_payload_inclinometer_x(short value) {
        setSIntBEElement(offsetBits_payload_inclinometer_x(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.inclinometer_x'
     */
    public static int size_payload_inclinometer_x() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.inclinometer_x'
     */
    public static int sizeBits_payload_inclinometer_x() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.inclinometer_y
    //   Field type: short
    //   Offset (bits): 160
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.inclinometer_y' is signed (false).
     */
    public static boolean isSigned_payload_inclinometer_y() {
        return false;
    }

    /**
     * Return whether the field 'payload.inclinometer_y' is an array (false).
     */
    public static boolean isArray_payload_inclinometer_y() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.inclinometer_y'
     */
    public static int offset_payload_inclinometer_y() {
        return (160 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.inclinometer_y'
     */
    public static int offsetBits_payload_inclinometer_y() {
        return 160;
    }

    /**
     * Return the value (as a short) of the field 'payload.inclinometer_y'
     */
    public short get_payload_inclinometer_y() {
        return (short)getSIntBEElement(offsetBits_payload_inclinometer_y(), 16);
    }

    /**
     * Set the value of the field 'payload.inclinometer_y'
     */
    public void set_payload_inclinometer_y(short value) {
        setSIntBEElement(offsetBits_payload_inclinometer_y(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.inclinometer_y'
     */
    public static int size_payload_inclinometer_y() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.inclinometer_y'
     */
    public static int sizeBits_payload_inclinometer_y() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.sam7_unixtime
    //   Field type: long
    //   Offset (bits): 176
    //   Size (bits): 32
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.sam7_unixtime' is signed (false).
     */
    public static boolean isSigned_payload_sam7_unixtime() {
        return false;
    }

    /**
     * Return whether the field 'payload.sam7_unixtime' is an array (false).
     */
    public static boolean isArray_payload_sam7_unixtime() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.sam7_unixtime'
     */
    public static int offset_payload_sam7_unixtime() {
        return (176 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.sam7_unixtime'
     */
    public static int offsetBits_payload_sam7_unixtime() {
        return 176;
    }

    /**
     * Return the value (as a long) of the field 'payload.sam7_unixtime'
     */
    public long get_payload_sam7_unixtime() {
        return (long)getUIntElement(offsetBits_payload_sam7_unixtime(), 32);
    }

    /**
     * Set the value of the field 'payload.sam7_unixtime'
     */
    public void set_payload_sam7_unixtime(long value) {
        setUIntElement(offsetBits_payload_sam7_unixtime(), 32, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.sam7_unixtime'
     */
    public static int size_payload_sam7_unixtime() {
        return (32 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.sam7_unixtime'
     */
    public static int sizeBits_payload_sam7_unixtime() {
        return 32;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.gps_sv_queue
    //   Field type: int
    //   Offset (bits): 208
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.gps_sv_queue' is signed (false).
     */
    public static boolean isSigned_payload_gps_sv_queue() {
        return false;
    }

    /**
     * Return whether the field 'payload.gps_sv_queue' is an array (false).
     */
    public static boolean isArray_payload_gps_sv_queue() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.gps_sv_queue'
     */
    public static int offset_payload_gps_sv_queue() {
        return (208 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.gps_sv_queue'
     */
    public static int offsetBits_payload_gps_sv_queue() {
        return 208;
    }

    /**
     * Return the value (as a int) of the field 'payload.gps_sv_queue'
     */
    public int get_payload_gps_sv_queue() {
        return (int)getUIntBEElement(offsetBits_payload_gps_sv_queue(), 16);
    }

    /**
     * Set the value of the field 'payload.gps_sv_queue'
     */
    public void set_payload_gps_sv_queue(int value) {
        setUIntBEElement(offsetBits_payload_gps_sv_queue(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.gps_sv_queue'
     */
    public static int size_payload_gps_sv_queue() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.gps_sv_queue'
     */
    public static int sizeBits_payload_gps_sv_queue() {
        return 16;
    }

}
