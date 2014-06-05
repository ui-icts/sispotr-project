package edu.uiowa.icts.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class FileUtil {

	static final int BUFFER = 2048;

	private static final Log log = LogFactory.getLog(FileUtil.class);
	
	public void mergePDFs(List<InputStream> files, String destinationFileName) throws COSVisitorException, IOException{
		PDFMergerUtility mergePdf = new PDFMergerUtility();
		mergePdf.addSources(files);
		mergePdf.setDestinationFileName(destinationFileName);
		mergePdf.mergeDocuments();
	}
	
	public void mergePDFs(List<InputStream> files, OutputStream destinationOutputStream) throws COSVisitorException, IOException{
		PDFMergerUtility mergePdf = new PDFMergerUtility();
		mergePdf.addSources(files);
		mergePdf.setDestinationStream(destinationOutputStream);
		mergePdf.mergeDocuments();
	}
	
	public File loadFileFromClasspath(String fileName){
		URL fileUrl = getClass().getResource(fileName);
		return new File(fileUrl.getFile());
	}
	
	public Document loadXpathDocument(File xmlFile, Map<String, String> uris) throws FileNotFoundException, DocumentException{
		FileInputStream is = new FileInputStream(xmlFile); 
		DocumentFactory factory = new DocumentFactory();
		factory.setXPathNamespaceURIs(uris);
		SAXReader reader = new SAXReader(false);
        reader.setDocumentFactory(factory);
        Document doc = reader.read(is);
        return doc;
	}
	
	public String setXPATHvalue(Element element, String xpath, boolean forceUpperCase) {
		if(xpath == null)
			return null;
		else
			return setXPATHvalue(element, xpath.toUpperCase());
	}
	
	public String setXPATHvalue(Element element, String xpath) {
		String result = null;
		
		if(xpath != null) {
			if (element.selectSingleNode("x:" + xpath) != null)
				result = element.selectSingleNode("x:" + xpath).getText();
			
			if (result != null && result.trim().length() == 0)
				result = null;
		}		

        return result;
	}
	
	public byte[] zip(ArrayList<String> filenames) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte data[] = new byte[BUFFER];
			BufferedInputStream input = null;
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(baos));
			
			ZipEntry entry;
			
			for (String filename : filenames) {
				log.debug("Adding Zip Entry: " + filename);
				File file = new File(filename);
				if(file != null && !file.isDirectory()){
					input = new BufferedInputStream(new FileInputStream(file), BUFFER);
					entry = new ZipEntry(file.getName());
					out.putNextEntry(entry);
					int count;
					while ((count = input.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					input.close();
				}
			}
			out.close();
		} catch (Exception e) {
			log.error("FileUtil: error zipping file(s)",e);
		}
		return baos.toByteArray();
	}
	
	public HashMap<String,byte[]> unZip(File zip_file) {
		HashMap<String,byte[]> files = new HashMap<String, byte[]>();
		try {
			ByteArrayOutputStream baos;
			InputStream is;
			ZipEntry entry;
			
			ZipFile zipfile = new ZipFile(zip_file);
			Enumeration<? extends ZipEntry> e = zipfile.entries();
			while (e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				is = zipfile.getInputStream(entry);
				baos = new ByteArrayOutputStream();
				copy(is, baos);
				files.put(entry.getName(), baos.toByteArray());
			}
		} catch (Exception e) {
			log.error("FileUtil: error unzipping file",e);
		}
		return files;
	}
	
	public void copy(InputStream inStream, OutputStream outStream) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(inStream);
			out = new BufferedOutputStream(outStream);
			while (true) {
				int data = in.read();
				if (data == -1) {
					break;
				}
	 			out.write(data);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
	
	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns false.
	public boolean delete(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = delete(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	}
	
	public void write(ArrayList<String> filenames, String fileName, byte[] data) throws IOException{
		write(fileName, data);
		filenames.add(fileName);
	}
	
	public void write(String fileName, byte[] data) throws IOException {
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.flush();
		fos.close();
	}
	
	public void write(ArrayList<String> filenames, File file, byte[] data) throws IOException{
		write(file, data);
		filenames.add(file.getName());
	}
	
	public void write(File file, byte[] data) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.flush();
		fos.close();
	}
}