/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'DozerEnviroScanIdentMsg'
 * message type.
 */

 package ch.epfl.gsn.wrappers.backlog.plugins.tinyos2x;

public class DozerEnviroScanIdentMsg extends DataHeaderMsg {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 22;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 182;

    /** Create a new DozerEnviroScanIdentMsg of size 22. */
    public DozerEnviroScanIdentMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new DozerEnviroScanIdentMsg of the given data_length. */
    public DozerEnviroScanIdentMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerEnviroScanIdentMsg with the given data_length
     * and base offset.
     */
    public DozerEnviroScanIdentMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerEnviroScanIdentMsg using the given byte array
     * as backing store.
     */
    public DozerEnviroScanIdentMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerEnviroScanIdentMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public DozerEnviroScanIdentMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerEnviroScanIdentMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public DozerEnviroScanIdentMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerEnviroScanIdentMsg embedded in the given message
     * at the given base offset.
     */
    public DozerEnviroScanIdentMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerEnviroScanIdentMsg embedded in the given message
     * at the given base offset and length.
     */
    public DozerEnviroScanIdentMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <DozerEnviroScanIdentMsg> \n";
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
        s += "  [payload.firmwareversion=";
        for (int i = 0; i < 3; i++) {
          s += "0x"+Long.toHexString(getElement_payload_firmwareversion(i) & 0xff)+" ";
        }
        s += "]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.serial=";
        for (int i = 0; i < 12; i++) {
          s += "0x"+Long.toHexString(getElement_payload_serial(i) & 0xff)+" ";
        }
        s += "]\n";
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
    // Accessor methods for field: payload.firmwareversion
    //   Field type: short[]
    //   Offset (bits): 56
    //   Size of each element (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.firmwareversion' is signed (false).
     */
    public static boolean isSigned_payload_firmwareversion() {
        return false;
    }

    /**
     * Return whether the field 'payload.firmwareversion' is an array (true).
     */
    public static boolean isArray_payload_firmwareversion() {
        return true;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.firmwareversion'
     */
    public static int offset_payload_firmwareversion(int index1) {
        int offset = 56;
        if (index1 < 0 || index1 >= 3) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return (offset / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.firmwareversion'
     */
    public static int offsetBits_payload_firmwareversion(int index1) {
        int offset = 56;
        if (index1 < 0 || index1 >= 3) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return offset;
    }

    /**
     * Return the entire array 'payload.firmwareversion' as a short[]
     */
    public short[] get_payload_firmwareversion() {
        short[] tmp = new short[3];
        for (int index0 = 0; index0 < numElements_payload_firmwareversion(0); index0++) {
            tmp[index0] = getElement_payload_firmwareversion(index0);
        }
        return tmp;
    }

    /**
     * Set the contents of the array 'payload.firmwareversion' from the given short[]
     */
    public void set_payload_firmwareversion(short[] value) {
        for (int index0 = 0; index0 < value.length; index0++) {
            setElement_payload_firmwareversion(index0, value[index0]);
        }
    }

    /**
     * Return an element (as a short) of the array 'payload.firmwareversion'
     */
    public short getElement_payload_firmwareversion(int index1) {
        return (short)getUIntBEElement(offsetBits_payload_firmwareversion(index1), 8);
    }

    /**
     * Set an element of the array 'payload.firmwareversion'
     */
    public void setElement_payload_firmwareversion(int index1, short value) {
        setUIntBEElement(offsetBits_payload_firmwareversion(index1), 8, value);
    }

    /**
     * Return the total size, in bytes, of the array 'payload.firmwareversion'
     */
    public static int totalSize_payload_firmwareversion() {
        return (24 / 8);
    }

    /**
     * Return the total size, in bits, of the array 'payload.firmwareversion'
     */
    public static int totalSizeBits_payload_firmwareversion() {
        return 24;
    }

    /**
     * Return the size, in bytes, of each element of the array 'payload.firmwareversion'
     */
    public static int elementSize_payload_firmwareversion() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of each element of the array 'payload.firmwareversion'
     */
    public static int elementSizeBits_payload_firmwareversion() {
        return 8;
    }

    /**
     * Return the number of dimensions in the array 'payload.firmwareversion'
     */
    public static int numDimensions_payload_firmwareversion() {
        return 1;
    }

    /**
     * Return the number of elements in the array 'payload.firmwareversion'
     */
    public static int numElements_payload_firmwareversion() {
        return 3;
    }

    /**
     * Return the number of elements in the array 'payload.firmwareversion'
     * for the given dimension.
     */
    public static int numElements_payload_firmwareversion(int dimension) {
      int array_dims[] = { 3,  };
        if (dimension < 0 || dimension >= 1) throw new ArrayIndexOutOfBoundsException();
        if (array_dims[dimension] == 0) throw new IllegalArgumentException("Array dimension "+dimension+" has unknown size");
        return array_dims[dimension];
    }

    /**
     * Fill in the array 'payload.firmwareversion' with a String
     */
    public void setString_payload_firmwareversion(String s) { 
         int len = s.length();
         int i;
         for (i = 0; i < len; i++) {
             setElement_payload_firmwareversion(i, (short)s.charAt(i));
         }
         setElement_payload_firmwareversion(i, (short)0); //null terminate
    }

    /**
     * Read the array 'payload.firmwareversion' as a String
     */
    public String getString_payload_firmwareversion() { 
         char carr[] = new char[Math.min(net.tinyos.message.Message.MAX_CONVERTED_STRING_LENGTH,3)];
         int i;
         for (i = 0; i < carr.length; i++) {
             if ((char)getElement_payload_firmwareversion(i) == (char)0) break;
             carr[i] = (char)getElement_payload_firmwareversion(i);
         }
         return new String(carr,0,i);
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.serial
    //   Field type: short[]
    //   Offset (bits): 80
    //   Size of each element (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.serial' is signed (false).
     */
    public static boolean isSigned_payload_serial() {
        return false;
    }

    /**
     * Return whether the field 'payload.serial' is an array (true).
     */
    public static boolean isArray_payload_serial() {
        return true;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.serial'
     */
    public static int offset_payload_serial(int index1) {
        int offset = 80;
        if (index1 < 0 || index1 >= 12) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return (offset / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.serial'
     */
    public static int offsetBits_payload_serial(int index1) {
        int offset = 80;
        if (index1 < 0 || index1 >= 12) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return offset;
    }

    /**
     * Return the entire array 'payload.serial' as a short[]
     */
    public short[] get_payload_serial() {
        short[] tmp = new short[12];
        for (int index0 = 0; index0 < numElements_payload_serial(0); index0++) {
            tmp[index0] = getElement_payload_serial(index0);
        }
        return tmp;
    }

    /**
     * Set the contents of the array 'payload.serial' from the given short[]
     */
    public void set_payload_serial(short[] value) {
        for (int index0 = 0; index0 < value.length; index0++) {
            setElement_payload_serial(index0, value[index0]);
        }
    }

    /**
     * Return an element (as a short) of the array 'payload.serial'
     */
    public short getElement_payload_serial(int index1) {
        return (short)getUIntBEElement(offsetBits_payload_serial(index1), 8);
    }

    /**
     * Set an element of the array 'payload.serial'
     */
    public void setElement_payload_serial(int index1, short value) {
        setUIntBEElement(offsetBits_payload_serial(index1), 8, value);
    }

    /**
     * Return the total size, in bytes, of the array 'payload.serial'
     */
    public static int totalSize_payload_serial() {
        return (96 / 8);
    }

    /**
     * Return the total size, in bits, of the array 'payload.serial'
     */
    public static int totalSizeBits_payload_serial() {
        return 96;
    }

    /**
     * Return the size, in bytes, of each element of the array 'payload.serial'
     */
    public static int elementSize_payload_serial() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of each element of the array 'payload.serial'
     */
    public static int elementSizeBits_payload_serial() {
        return 8;
    }

    /**
     * Return the number of dimensions in the array 'payload.serial'
     */
    public static int numDimensions_payload_serial() {
        return 1;
    }

    /**
     * Return the number of elements in the array 'payload.serial'
     */
    public static int numElements_payload_serial() {
        return 12;
    }

    /**
     * Return the number of elements in the array 'payload.serial'
     * for the given dimension.
     */
    public static int numElements_payload_serial(int dimension) {
      int array_dims[] = { 12,  };
        if (dimension < 0 || dimension >= 1) throw new ArrayIndexOutOfBoundsException();
        if (array_dims[dimension] == 0) throw new IllegalArgumentException("Array dimension "+dimension+" has unknown size");
        return array_dims[dimension];
    }

    /**
     * Fill in the array 'payload.serial' with a String
     */
    public void setString_payload_serial(String s) { 
         int len = s.length();
         int i;
         for (i = 0; i < len; i++) {
             setElement_payload_serial(i, (short)s.charAt(i));
         }
         setElement_payload_serial(i, (short)0); //null terminate
    }

    /**
     * Read the array 'payload.serial' as a String
     */
    public String getString_payload_serial() { 
         char carr[] = new char[Math.min(net.tinyos.message.Message.MAX_CONVERTED_STRING_LENGTH,12)];
         int i;
         for (i = 0; i < carr.length; i++) {
             if ((char)getElement_payload_serial(i) == (char)0) break;
             carr[i] = (char)getElement_payload_serial(i);
         }
         return new String(carr,0,i);
    }

}
