package com.aws.boot.angular.rest.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aws.boot.angular.rest.exception.FileArchiveServiceException;
import com.aws.boot.angular.rest.model.CustomerImage;

/**
* @author Krishna Bhat
*
*/

@Service
public class FileArchiveService {

	public CustomerImage saveFile(MultipartFile multipartFile) {

		try{
			File fileToUpload = convertFromMultiPart(multipartFile);
			String key = Instant.now().getEpochSecond() + "_" + fileToUpload.getName();
			String url = fileToUpload.getPath();
			/* save file */
			//... file saved to disk
			
			return new CustomerImage(key, url);
		}
		catch(Exception ex){			
			throw new FileArchiveServiceException("An error occurred saving file to.", ex);
		}		
	}

	public void deleteImage(CustomerImage customerImage){
		//image deleted
	}

	/**
	 * Convert MultiPartFile to ordinary File
	 * 
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	private File convertFromMultiPart(MultipartFile multipartFile) throws IOException {

		File file = new File(multipartFile.getOriginalFilename());
		file.createNewFile(); 
		FileOutputStream fos = new FileOutputStream(file); 
		fos.write(multipartFile.getBytes());
		fos.close(); 

		return file;
	}
}