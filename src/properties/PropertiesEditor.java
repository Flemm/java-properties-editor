/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package properties;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * An editor of files in a Property format (a file consisting of key/value
 * pairs). This class can be decorated by some known classes like
 * AutoCommitEditor. You
 * must call commit() to save your changes to the selected file.
 *
 * @author Felipe Lacerda Edington
 */
public class PropertiesEditor {
    private Properties props;
    private Properties checkpoint;
    private File file;
    private String baseDirectory = "";
    private PropertiesEditor decorator;

    /**
     * The simplest constructor, only instantiating a new Properties object.
     * Every other one of this class constructors should call this one first
     */
    public PropertiesEditor() {
        props = new Properties();
    }

    /**
     * A constructor that already selects a file based on a File object.
     *
     * @param file is the file object to be selected.
     */
    public PropertiesEditor(final File file) {
        this();
        selectFile(file);
    }

    /**
     * A constructor that already selects a file based on a file path.
     *
     * @param filepath is the path to the file to be selected.
     */
    public PropertiesEditor(final String filepath) {
        this();
        selectFile(filepath);
    }

    /**
     * This method sets a state to go back to on a rollback() call. This state
     * is deleted when you sucessfully execute a commit() call;
     */
    public void beginTransaction() {
        checkpoint = getCheckpoint();
    }

    /**
     * Writes the changes to the selected file.
     *
     * @return true if it sucessfully commited changes to file, false otherwise
     */
    public boolean commit() {
        try {
            final FileOutputStream fos = new FileOutputStream(file);
            props.store(fos, null);
            fos.close();
            clearCheckpoint();
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Creates the selected file if it doesn't exists. Only creates files, not
     * directories.
     *
     * @return true if it managed to create the file.
     * @throws IOException when something goes wrong in the writing to the disk.
     */
    public boolean createPropertiesFile() throws IOException {
        if (!fileExists() && !file.isDirectory()) {
            return file.createNewFile();
        }
        return false;
    }

    /**
     * Deletes the selected file from the disk. This action CANNOT be reversed.
     *
     * @return true if the file was sucessfully deleted.
     * @throws IOException if something went wrong in the deletion
     */
    public boolean deletePropertiesFile() throws IOException {
        if (fileExists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Erases a key from the Property data in memory.
     *
     * @param key the key of the pair to be removed.
     */
    public void erase(final String key) {
        props.remove(key);
    }

    /**
     * Checks if the selected file exists.
     *
     * @return true if it exists.
     */
    public boolean fileExists() {
        if (file != null && file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * Gets the base directory.
     *
     * @return the base directory.
     */
    public String getBaseDirectory() {
        return baseDirectory;
    }

    private Properties getCheckpoint() {
        return props;
    }
    
    private void clearCheckpoint(){
        this.checkpoint = null;
    }

    protected PropertiesEditor getDecorator() {
        return decorator;
    }

    /**
     * Loads the selected file into memory. You must do this in order to be able
     * to read/write information in the properties.
     *
     * @return true if it managed to load the file.
     */
    public boolean load() {
        try {
            return load(file);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean load(final File file) throws FileNotFoundException {
        props.clear();

        final FileInputStream fis = new FileInputStream(file);

        try {
            props.load(fis);
            fis.close();
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tries to read a value from the Property data in memory.
     *
     * @param key the key that identifies the value.
     * @return the value.
     */
    public String read(final String key) {
        return read(key, "");
    }

    /**
     * Tries to read a value from the Property data in memory, but if it's not
     * found then returns a default value.
     *
     * @param key the key that identifies the value.
     * @param defaultValue the value to be return if the searched key doesn't
     *        exists.
     *
     * @return either the read value or the default one.
     */
    public String read(final String key, final String defaultValue) {
        final String result = props.getProperty(key, defaultValue);
        return result;
    }

    /**
     * This method rolls back to the state setted by a beginTransaction() call.
     */
    public void rollback() {
        if (checkpoint != null) {
            props = checkpoint;
        }
        clearCheckpoint();
    }

    /**
     * Selects a file to be used from a File object.
     *
     * @param file the File object.
     */
    public void selectFile(final File file) {
        this.file = file;
    }

    /**
     * Selects a file to be used from a path. If there's a base directory
     * defined, the path must be relative to it.
     *
     * @param filepath the path to the file, relative to a possible base
     *        directory.
     */
    public void selectFile(final String filepath) {
        selectFile(new File(baseDirectory + filepath));
    }

    /**
     * Sets a path that shall be the base directory. Future tries to select
     * files from a path must keep in mind that the path has to be relative to
     * this base
     * directory.
     *
     * @param baseDirectory the base directory
     */
    public void setBaseDirectory(final String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    protected void setDecorator(final PropertiesEditor decorator) {
        this.decorator = decorator;
    }

    /**
     * Writes a pair of key and value to the Property data in memory.
     *
     * @param key the key to be written.
     * @param value the value to be written.
     */
    public void write(final String key, final String value) {
        props.put(key, value);
    }
}
