/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package properties;

/**
 * This class is a decorator (wrapper) for PropertiesEditor.
 * It adds the auto-save functionality so that you don't have to call commit() everytime
 * anymore.
 *
 * @author Felipe Lacerda Edington
 */
public class AutoCommitEditor extends PropertiesEditor {

    public AutoCommitEditor(final PropertiesEditor decorator) {
        setDecorator(decorator);
    }

    @Override
    public void erase(final String key) {
        getDecorator().beginTransaction();
        getDecorator().erase(key);
        if (!commit()) {
            rollback();
            throw new RuntimeException("Couldn't commit on erase");
        }
    }

    @Override
    public void write(final String key, final String value) {
        getDecorator().beginTransaction();
        getDecorator().write(key, value);
        if (!commit()) {
            rollback();
            throw new RuntimeException("Couldn't commit on write");
        }
    }
    
    @Override
    public void rollback(){
        getDecorator().rollback();
    }

}
