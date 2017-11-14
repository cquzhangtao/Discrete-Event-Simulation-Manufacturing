package basic.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import enterprise.materialflow.simulation.EnterpriseSimulation;

public class Snapshot {

	public static <T> void snapshot(String name, T obj) {

		ObjectOutputStream objectOutputStream;
		try {
			
			FileOutputStream outStream = new FileOutputStream(new File(name));
			objectOutputStream = new ObjectOutputStream(outStream);
			objectOutputStream.writeObject(obj);
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static <T> T restore(String name) {
		try {
			FileInputStream freader = new FileInputStream(new File(name));
			ObjectInputStream objectInputStream = new ObjectInputStream(freader);
			T obj = (T) objectInputStream.readObject();
			objectInputStream.close();
			return obj;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
