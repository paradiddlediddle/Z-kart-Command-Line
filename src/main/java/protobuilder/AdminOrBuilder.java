// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: compiler/Zkart.proto

package protobuilder;

public interface AdminOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Zkart.Admin)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>required int64 id = 1;</code>
   * @return Whether the id field is set.
   */
  boolean hasId();
  /**
   * <code>required int64 id = 1;</code>
   * @return The id.
   */
  long getId();

  /**
   * <code>required string name = 2;</code>
   * @return Whether the name field is set.
   */
  boolean hasName();
  /**
   * <code>required string name = 2;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>required string name = 2;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>required string email = 3;</code>
   * @return Whether the email field is set.
   */
  boolean hasEmail();
  /**
   * <code>required string email = 3;</code>
   * @return The email.
   */
  java.lang.String getEmail();
  /**
   * <code>required string email = 3;</code>
   * @return The bytes for email.
   */
  com.google.protobuf.ByteString
      getEmailBytes();

  /**
   * <code>required string password = 4;</code>
   * @return Whether the password field is set.
   */
  boolean hasPassword();
  /**
   * <code>required string password = 4;</code>
   * @return The password.
   */
  java.lang.String getPassword();
  /**
   * <code>required string password = 4;</code>
   * @return The bytes for password.
   */
  com.google.protobuf.ByteString
      getPasswordBytes();

  /**
   * <code>optional int64 phoneNumber = 5;</code>
   * @return Whether the phoneNumber field is set.
   */
  boolean hasPhoneNumber();
  /**
   * <code>optional int64 phoneNumber = 5;</code>
   * @return The phoneNumber.
   */
  long getPhoneNumber();
}
