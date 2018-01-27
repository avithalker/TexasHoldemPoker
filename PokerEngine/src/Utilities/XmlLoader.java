package Utilities;

import generatedJaxb.GameDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class XmlLoader {

    public static GameDescriptor LoadGameSettings(String filePath)throws JAXBException {

        File file = new File(filePath);
        JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        GameDescriptor settings = (GameDescriptor) jaxbUnmarshaller.unmarshal(file);
        return settings;
    }

    public static GameDescriptor LoadGameSettingsFromStr(String settingsContent) throws JAXBException{
        InputStream settingsStream = new ByteArrayInputStream(settingsContent.getBytes());
        JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        GameDescriptor settings = (GameDescriptor) jaxbUnmarshaller.unmarshal(settingsStream);
        return settings;
    }
}
