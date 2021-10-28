package de.hhn.it.pp.components.astarpathfinding.exceptions;

import java.security.PrivilegedActionException;

public class PositionOutOfBounds extends Exception {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(PositionOutOfBounds.class);

  public enum PositionType {
    START,
    DESTINATION,
    DEFAULT
  }

  private PositionType positionType = PositionType.DEFAULT;

  /**
   * Constructs a new exception with {@code null} as its detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   */
  public PositionOutOfBounds() {}

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and
   * may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public PositionOutOfBounds(String message) {
    super(message);
  }

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and
   * may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   * @param type the type of the affected position
   */
  public PositionOutOfBounds(final String message, PositionType type) {
    super(message);
    positionType = type;
  }

  /**
   * Constructs a new exception with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated in this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method).
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or
   *     unknown.)
   * @since 1.4
   */
  public PositionOutOfBounds(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new exception with the specified cause and a detail message of <tt>(cause==null ?
   * null : cause.toString())</tt> (which typically contains the class and detail message of
   * <tt>cause</tt>). This constructor is useful for exceptions that are little more than wrappers
   * for other throwables (for example, {@link PrivilegedActionException}).
   *
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or
   *     unknown.)
   * @since 1.4
   */
  public PositionOutOfBounds(final Throwable cause) {
    super(cause);
  }

  public PositionType getPositionType() {
    return positionType;
  }
}
