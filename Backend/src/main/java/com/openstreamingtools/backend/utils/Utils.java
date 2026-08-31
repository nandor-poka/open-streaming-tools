package com.openstreamingtools.backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.backend.messages.frontend.SongData;
import com.openstreamingtools.backend.messaging.SongDataUpdateTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * General-purpose utility class providing static helper methods for common operations
 * including byte array conversions, UUID handling, network byte operations, and task scheduling.
 *
 * <p>Contains shared static resources:
 * <ul>
 *   <li>{@link #objectMapper} - Jackson ObjectMapper for JSON serialization</li>
 *   <li>{@link #restClient} - Spring RestClient for HTTP requests</li>
 *   <li>{@link #timer} - Java Timer for scheduled tasks</li>
 *   <li>{@link #taskQueue} - Queue for song data update tasks</li>
 *   <li>{@link #logDataQueue} - Queue for song data logging</li>
 *   <li>{@link #UIUpdateSchedulerThread} - Background thread for UI updates</li>
 *   <li>{@link #SongDataLoggerThread} - Background thread for data logging</li>
 * </ul>
 */
@Slf4j
public class Utils {

    /** Shared Jackson ObjectMapper for JSON processing */
    public static final ObjectMapper objectMapper = new ObjectMapper();
    /** Shared Java Timer for scheduling periodic tasks */
    public static final Timer timer = new Timer();
    /** Shared Spring RestClient for making HTTP requests */
    public static RestClient restClient = RestClient.create();
    /** Queue for pending song data update tasks */
    public static final ArrayBlockingQueue<SongDataUpdateTask> taskQueue = new ArrayBlockingQueue<>(32, true);
    /** Queue for song data to be logged */
    public static final ArrayBlockingQueue<SongData> logDataQueue = new ArrayBlockingQueue<>(32, true);
    private static final Vector<SongDataUpdateTask> currentlyScheduledTasks = new Vector<>(8);
    /** Background thread for processing UI updates */
    public static final Thread UIUpdateSchedulerThread = new Thread(new UIUpdateScheduler());
    /** Background thread for logging song data */
    public static final Thread SongDataLoggerThread = new Thread(new SongDataLogger());
    private static final ReentrantLock taskReentrantLock = new ReentrantLock(true);
    private static final ReentrantLock logDataReentrantLock = new ReentrantLock(true);
    /** Duration of one hour in milliseconds (3,600,000 ms) */
    public static final long HOUR_IN_MILLIS = 3600000;


    /**
     * Converts an integer into a byte array and stores it at the specified position.
     * Stores the bytes in big-endian (network) byte order.
     *
     * @param i the integer value to convert
     * @param array the byte array where the result will be stored (must have at least 4 elements)
     */
    public static void putIntegerToByteArray(int i, byte[] array){
       array[0] = (byte)((i >> 24)& 0xFF);
       array[1] = (byte)((i >> 16)& 0xFF);
       array[2] = (byte)((i >> 8)& 0xFF);
       array[3] = (byte)(i & 0xFF);
    }

    /**
     * Converts an integer to a 4-byte array in big-endian (network) byte order.
     *
     * @param i the integer value to convert
     * @return a 4-byte array representation of the integer
     */
    public static byte[] convertIntegerToByteArray(int i){
       byte[] bytes = new byte[4];
       bytes[0] = (byte)((i >> 24)& 0xFF);
       bytes[1] = (byte)((i >> 16)& 0xFF);
       bytes[2] = (byte)((i >> 8)& 0xFF);
       bytes[3] = (byte)(i & 0xFF);
       return bytes;
    }

    /**
     * Converts a short integer to a 2-byte array in big-endian (network) byte order.
     *
     * @param i the integer value representing a short to convert
     * @return a 2-byte array representation of the short
     */
    public static byte[] convertShortToByteArray(int i){
       byte[] bytes = new byte[2];
       bytes[0] = (byte)((i >> 8)& 0xFF);
       bytes[1] = (byte)(i & 0xFF);
       return bytes;
    }

    /**
     * Converts a UUID to a 16-byte array representation using big-endian byte order.
     *
     * @param uuid the UUID to convert
     * @return a 16-byte array containing the UUID's most and least significant bits
     */
    public static byte[] convertUUIDToBytes(UUID uuid) {
       ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
       bb.putLong(uuid.getMostSignificantBits());
       bb.putLong(uuid.getLeastSignificantBits());
       return bb.array();
    }

    /**
     * Reconstructs a UUID from a 16-byte array.
     *
     * @param bytes a 16-byte array containing UUID data
     * @return the reconstructed UUID
     */
    public static UUID convertBytesToUUID(byte[] bytes) {
       ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
       long high = byteBuffer.getLong();
       long low = byteBuffer.getLong();
       return new UUID(high, low);
    }

    /**
     * Converts a string to network bytes with a 4-byte length prefix in the specified charset.
     * Format: [4-byte length][string bytes]
     *
     * @param string the string to convert
     * @param charset the character set to use for encoding
     * @return a byte array with 4-byte length prefix followed by encoded string bytes
     */
    public static byte[] getNetworkBytesWithLenghForString(String string, Charset charset){
       byte[] stringAsBytes = string.getBytes(charset);
       byte[] intAsBytes = convertIntegerToByteArray(stringAsBytes.length);
       byte[] networkBytes = new byte[4+stringAsBytes.length];

       // add length first
       for (int i=0;i<intAsBytes.length;i++){
           networkBytes[i] = intAsBytes[i];
       }

       for (int i = 0; i < stringAsBytes.length; i++){
           networkBytes[i+4] = stringAsBytes[i];
       }
       return networkBytes;
    }

    /**
     * Converts a 4-byte array to an integer in big-endian (network) byte order.
     *
     * @param bytes a 4-byte array to convert
     * @return the integer value
     */
    public static int convertBytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
     }

    /**
     * Converts a 2-byte array to a short integer in big-endian (network) byte order.
     *
     * @param bytes a 2-byte array to convert
     * @return the short integer value
     */
    public static int convertBytesToShort(byte[] bytes) {
       return ByteBuffer.wrap(bytes).getShort();
    }

    /**
     * Adds a song data update task to the currently scheduled tasks list.
     * Thread-safe using a reentrant lock.
     *
     * @param task the task to add
     */
    public static void addToScheduledTasks(SongDataUpdateTask task){
       taskReentrantLock.lock();
       currentlyScheduledTasks.add(task);
       taskReentrantLock.unlock();
    }

    /**
     * Removes a song data update task from the scheduled tasks list.
     * Thread-safe using a reentrant lock.
     *
     * @param task the task to remove
     * @return true if the task was removed, false if it was not in the list
     */
    public static boolean removeScheduledTask(SongDataUpdateTask task){
       taskReentrantLock.lock();
       boolean result = currentlyScheduledTasks.remove(task);
       taskReentrantLock.unlock();
        return result;
    }

    /**
     * Checks if a task is currently in the scheduled tasks list.
     * Thread-safe using a reentrant lock.
     *
     * @param task the task to check for
     * @return true if the task is scheduled, false otherwise
     */
    public static boolean isCurrentlyScheduled(SongDataUpdateTask task){
        taskReentrantLock.lock();
        boolean result = currentlyScheduledTasks.contains(task);
        taskReentrantLock.unlock();
        return result;
    }

    /**
     * Retrieves a scheduled task that matches the given task using equals comparison.
     * Thread-safe using a reentrant lock.
     *
     * @param task the task to find
     * @return an Optional containing the matching task if found, empty otherwise
     */
    public static Optional<SongDataUpdateTask> getScheduledTask(SongDataUpdateTask task){
        taskReentrantLock.lock();
        Optional<SongDataUpdateTask>  result = currentlyScheduledTasks.stream()
            .filter(e -> e.equals(task)).findFirst();
        taskReentrantLock.unlock();
        return result;
    }

    /**
     * Drains all song data items from the log data queue into a new list.
     * Thread-safe using a reentrant lock.
     *
     * @return a list of all song data currently in the log queue, empty if queue is empty
     */
    public static List<SongData> getCurrentSongDataToLog(){
        logDataReentrantLock.lock();
        List<SongData> currentSongData = new ArrayList<>();
        logDataQueue.drainTo(currentSongData);
        logDataReentrantLock.unlock();
        return currentSongData;

    }
}
