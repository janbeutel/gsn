/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'DozerWgpsStatusMsg'
 * message type.
 */

package ch.ethz.permasense.tinyos2x.messages;

public class DozerWgpsStatusMsg extends ch.ethz.permasense.tinyos2x.messages.DataHeaderMsg {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 30;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 188;

    /** Create a new DozerWgpsStatusMsg of size 30. */
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
        s += "  [payload.gpsvoltage=0x"+Long.toHexString(get_payload_gpsvoltage())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.gpscurrent=0x"+Long.toHexString(get_payload_gpscurrent())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.sam7current=0x"+Long.toHexString(get_payload_sam7current())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.tncurrent=0x"+Long.toHexString(get_payload_tncurrent())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.inclinometerx=0x"+Long.toHexString(get_payload_inclinometerx())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.inclinometery=0x"+Long.toHexString(get_payload_inclinometery())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.numsv=0x"+Long.toHexString(get_payload_numsv())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.gpsweek=0x"+Long.toHexString(get_payload_gpsweek())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.gpstime=0x"+Long.toHexString(get_payload_gpstime())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.crc=0x"+Long.toHexString(get_payload_crc())+"]\n";
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
    // Accessor methods for field: payload.gpsvoltage
    //   Field type: int
    //   Offset (bits): 72
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.gpsvoltage' is signed (false).
     */
    public static boolean isSigned_payload_gpsvoltage() {
        return false;
    }

    /**
     * Return whether the field 'payload.gpsvoltage' is an array (false).
     */
    public static boolean isArray_payload_gpsvoltage() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.gpsvoltage'
     */
    public static int offset_payload_gpsvoltage() {
        return (72 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.gpsvoltage'
     */
    public static int offsetBits_payload_gpsvoltage() {
        return 72;
    }

    /**
     * Return the value (as a int) of the field 'payload.gpsvoltage'
     */
    public int get_payload_gpsvoltage() {
        return (int)getUIntBEElement(offsetBits_payload_gpsvoltage(), 16);
    }

    /**
     * Set the value of the field 'payload.gpsvoltage'
     */
    public void set_payload_gpsvoltage(int value) {
        setUIntBEElement(offsetBits_payload_gpsvoltage(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.gpsvoltage'
     */
    public static int size_payload_gpsvoltage() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.gpsvoltage'
     */
    public static int sizeBits_payload_gpsvoltage() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.gpscurrent
    //   Field type: int
    //   Offset (bits): 88
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.gpscurrent' is signed (false).
     */
    public static boolean isSigned_payload_gpscurrent() {
        return false;
    }

    /**
     * Return whether the field 'payload.gpscurrent' is an array (false).
     */
    public static boolean isArray_payload_gpscurrent() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.gpscurrent'
     */
    public static int offset_payload_gpscurrent() {
        return (88 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.gpscurrent'
     */
    public static int offsetBits_payload_gpscurrent() {
        return 88;
    }

    /**
     * Return the value (as a int) of the field 'payload.gpscurrent'
     */
    public int get_payload_gpscurrent() {
        return (int)getUIntBEElement(offsetBits_payload_gpscurrent(), 16);
    }

    /**
     * Set the value of the field 'payload.gpscurrent'
     */
    public void set_payload_gpscurrent(int value) {
        setUIntBEElement(offsetBits_payload_gpscurrent(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.gpscurrent'
     */
    public static int size_payload_gpscurrent() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.gpscurrent'
     */
    public static int sizeBits_payload_gpscurrent() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.sam7current
    //   Field type: int
    //   Offset (bits): 104
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.sam7current' is signed (false).
     */
    public static boolean isSigned_payload_sam7current() {
        return false;
    }

    /**
     * Return whether the field 'payload.sam7current' is an array (false).
     */
    public static boolean isArray_payload_sam7current() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.sam7current'
     */
    public static int offset_payload_sam7current() {
        return (104 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.sam7current'
     */
    public static int offsetBits_payload_sam7current() {
        return 104;
    }

    /**
     * Return the value (as a int) of the field 'payload.sam7current'
     */
    public int get_payload_sam7current() {
        return (int)getUIntBEElement(offsetBits_payload_sam7current(), 16);
    }

    /**
     * Set the value of the field 'payload.sam7current'
     */
    public void set_payload_sam7current(int value) {
        setUIntBEElement(offsetBits_payload_sam7current(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.sam7current'
     */
    public static int size_payload_sam7current() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.sam7current'
     */
    public static int sizeBits_payload_sam7current() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.tncurrent
    //   Field type: int
    //   Offset (bits): 120
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.tncurrent' is signed (false).
     */
    public static boolean isSigned_payload_tncurrent() {
        return false;
    }

    /**
     * Return whether the field 'payload.tncurrent' is an array (false).
     */
    public static boolean isArray_payload_tncurrent() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.tncurrent'
     */
    public static int offset_payload_tncurrent() {
        return (120 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.tncurrent'
     */
    public static int offsetBits_payload_tncurrent() {
        return 120;
    }

    /**
     * Return the value (as a int) of the field 'payload.tncurrent'
     */
    public int get_payload_tncurrent() {
        return (int)getUIntBEElement(offsetBits_payload_tncurrent(), 16);
    }

    /**
     * Set the value of the field 'payload.tncurrent'
     */
    public void set_payload_tncurrent(int value) {
        setUIntBEElement(offsetBits_payload_tncurrent(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.tncurrent'
     */
    public static int size_payload_tncurrent() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.tncurrent'
     */
    public static int sizeBits_payload_tncurrent() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.inclinometerx
    //   Field type: short
    //   Offset (bits): 136
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.inclinometerx' is signed (false).
     */
    public static boolean isSigned_payload_inclinometerx() {
        return false;
    }

    /**
     * Return whether the field 'payload.inclinometerx' is an array (false).
     */
    public static boolean isArray_payload_inclinometerx() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.inclinometerx'
     */
    public static int offset_payload_inclinometerx() {
        return (136 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.inclinometerx'
     */
    public static int offsetBits_payload_inclinometerx() {
        return 136;
    }

    /**
     * Return the value (as a short) of the field 'payload.inclinometerx'
     */
    public short get_payload_inclinometerx() {
        return (short)getSIntBEElement(offsetBits_payload_inclinometerx(), 16);
    }

    /**
     * Set the value of the field 'payload.inclinometerx'
     */
    public void set_payload_inclinometerx(short value) {
        setSIntBEElement(offsetBits_payload_inclinometerx(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.inclinometerx'
     */
    public static int size_payload_inclinometerx() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.inclinometerx'
     */
    public static int sizeBits_payload_inclinometerx() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.inclinometery
    //   Field type: short
    //   Offset (bits): 152
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.inclinometery' is signed (false).
     */
    public static boolean isSigned_payload_inclinometery() {
        return false;
    }

    /**
     * Return whether the field 'payload.inclinometery' is an array (false).
     */
    public static boolean isArray_payload_inclinometery() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.inclinometery'
     */
    public static int offset_payload_inclinometery() {
        return (152 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.inclinometery'
     */
    public static int offsetBits_payload_inclinometery() {
        return 152;
    }

    /**
     * Return the value (as a short) of the field 'payload.inclinometery'
     */
    public short get_payload_inclinometery() {
        return (short)getSIntBEElement(offsetBits_payload_inclinometery(), 16);
    }

    /**
     * Set the value of the field 'payload.inclinometery'
     */
    public void set_payload_inclinometery(short value) {
        setSIntBEElement(offsetBits_payload_inclinometery(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.inclinometery'
     */
    public static int size_payload_inclinometery() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.inclinometery'
     */
    public static int sizeBits_payload_inclinometery() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.numsv
    //   Field type: short
    //   Offset (bits): 168
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.numsv' is signed (false).
     */
    public static boolean isSigned_payload_numsv() {
        return false;
    }

    /**
     * Return whether the field 'payload.numsv' is an array (false).
     */
    public static boolean isArray_payload_numsv() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.numsv'
     */
    public static int offset_payload_numsv() {
        return (168 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.numsv'
     */
    public static int offsetBits_payload_numsv() {
        return 168;
    }

    /**
     * Return the value (as a short) of the field 'payload.numsv'
     */
    public short get_payload_numsv() {
        return (short)getUIntElement(offsetBits_payload_numsv(), 8);
    }

    /**
     * Set the value of the field 'payload.numsv'
     */
    public void set_payload_numsv(short value) {
        setUIntElement(offsetBits_payload_numsv(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.numsv'
     */
    public static int size_payload_numsv() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.numsv'
     */
    public static int sizeBits_payload_numsv() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.gpsweek
    //   Field type: int
    //   Offset (bits): 176
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.gpsweek' is signed (false).
     */
    public static boolean isSigned_payload_gpsweek() {
        return false;
    }

    /**
     * Return whether the field 'payload.gpsweek' is an array (false).
     */
    public static boolean isArray_payload_gpsweek() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.gpsweek'
     */
    public static int offset_payload_gpsweek() {
        return (176 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.gpsweek'
     */
    public static int offsetBits_payload_gpsweek() {
        return 176;
    }

    /**
     * Return the value (as a int) of the field 'payload.gpsweek'
     */
    public int get_payload_gpsweek() {
        return (int)getUIntElement(offsetBits_payload_gpsweek(), 16);
    }

    /**
     * Set the value of the field 'payload.gpsweek'
     */
    public void set_payload_gpsweek(int value) {
        setUIntElement(offsetBits_payload_gpsweek(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.gpsweek'
     */
    public static int size_payload_gpsweek() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.gpsweek'
     */
    public static int sizeBits_payload_gpsweek() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.gpstime
    //   Field type: long
    //   Offset (bits): 192
    //   Size (bits): 32
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.gpstime' is signed (false).
     */
    public static boolean isSigned_payload_gpstime() {
        return false;
    }

    /**
     * Return whether the field 'payload.gpstime' is an array (false).
     */
    public static boolean isArray_payload_gpstime() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.gpstime'
     */
    public static int offset_payload_gpstime() {
        return (192 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.gpstime'
     */
    public static int offsetBits_payload_gpstime() {
        return 192;
    }

    /**
     * Return the value (as a long) of the field 'payload.gpstime'
     */
    public long get_payload_gpstime() {
        return (long)getUIntElement(offsetBits_payload_gpstime(), 32);
    }

    /**
     * Set the value of the field 'payload.gpstime'
     */
    public void set_payload_gpstime(long value) {
        setUIntElement(offsetBits_payload_gpstime(), 32, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.gpstime'
     */
    public static int size_payload_gpstime() {
        return (32 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.gpstime'
     */
    public static int sizeBits_payload_gpstime() {
        return 32;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.crc
    //   Field type: int
    //   Offset (bits): 224
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.crc' is signed (false).
     */
    public static boolean isSigned_payload_crc() {
        return false;
    }

    /**
     * Return whether the field 'payload.crc' is an array (false).
     */
    public static boolean isArray_payload_crc() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.crc'
     */
    public static int offset_payload_crc() {
        return (224 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.crc'
     */
    public static int offsetBits_payload_crc() {
        return 224;
    }

    /**
     * Return the value (as a int) of the field 'payload.crc'
     */
    public int get_payload_crc() {
        return (int)getUIntElement(offsetBits_payload_crc(), 16);
    }

    /**
     * Set the value of the field 'payload.crc'
     */
    public void set_payload_crc(int value) {
        setUIntElement(offsetBits_payload_crc(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.crc'
     */
    public static int size_payload_crc() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.crc'
     */
    public static int sizeBits_payload_crc() {
        return 16;
    }

}
