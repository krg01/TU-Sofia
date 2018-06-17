package com.config.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.api.awt.SequenceEncoder;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.NIOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.config.WebInitializer;
import com.config.dao.VideoDAO;
import com.config.model.User;

@Controller
@SessionAttributes("user")
public class UploadController {

	private static final String POSTERS_LOCATION = "C:\\DEV\\posters";
	private static final String VIDEOS_LOCATION = "C:\\DEV\\videos";

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String prepareForUpload() {
		return "upload";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String receiveUpload(@RequestParam("video") MultipartFile multiPartFile,
			@RequestParam("videoName") String videoName, @RequestParam("category") String category,
			@RequestParam("description") String description, Model model, HttpSession ses) throws IOException {

		if (multiPartFile.getSize() > WebInitializer.MAX_FILE_SIZE) {
			model.addAttribute("status", "File size is > 500MB");
			return "upload";
		}

		if (videoName == null || videoName.isEmpty() || videoName.length() > 100) {

			model.addAttribute("status", "Invalid video name.");

			return "upload";
		}
		if (category == null || category.isEmpty() || category.length() > 50) {
			model.addAttribute("status", "Invalid category.");
			return "upload";
		}
		if (description == null || description.isEmpty() || description.length() > 220) {
			model.addAttribute("status", "Invalid description.");
			return "upload";
		}

		if (!VideoDAO.getInstance().isFreeVideoName(videoName)) {
			model.addAttribute("status", "Video with this name already exist !");
			return "upload";
		}

		User user = (User) ses.getAttribute("user");

		if (!validateVideoFormat(multiPartFile.getContentType())) {
			System.out.println("invalid format");
			model.addAttribute("status", "Invalid video format.");
			return "upload";
		}

		String type = ".";
		String[] format = multiPartFile.getContentType().split("/");
		if (format[0].equals("video")) {
			type += format[1];
		}

		String fileFullName = videoName.concat(type);
		String poster = videoName.concat(".jpg");
		File videoDir = new File(VIDEOS_LOCATION);
		if (!videoDir.exists()) {
			videoDir.mkdir();
		}
		File videoFile = new File(videoDir, fileFullName);
		Files.copy(multiPartFile.getInputStream(), videoFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		try {
			st(videoFile, poster);
		} catch (JCodecException e) {
			e.printStackTrace();
		}

		model.addAttribute("status", "Video: " + videoName + " uploaded.");
		String address = VIDEOS_LOCATION.concat("/").concat(fileFullName);
		String posterLocation = POSTERS_LOCATION.concat("/").concat(poster); // todo
																				// set																				// DB
		VideoDAO.getInstance().uploadVideo(videoName, category, description, user.getUsername(), address,
				posterLocation);
		return "upload";
	}

	private void st(File videoFile, String poster) throws IOException, JCodecException {
		File posterDir = new File(POSTERS_LOCATION);
		if (!posterDir.exists()) {
			posterDir.mkdir();
		}

		double msec = 5000;

		BufferedImage frame = getFrame(videoFile, 10);
		ImageIO.write(frame, "jpg", new File(posterDir, poster));
	}

	private BufferedImage getFrame(File file, double sec) throws IOException, JCodecException {
		FileChannelWrapper ch = null;
		try {
			ch = NIOUtils.readableFileChannel(file);
			return ((FrameGrab) new FrameGrab(ch).seekToSecondPrecise(sec)).getFrame();
		} finally {
			NIOUtils.closeQuietly(ch);
		}
	}

	private boolean validateVideoFormat(String contentType) {
		String[] contentParams = contentType.split("/");
		if (contentParams.length < 2 || contentParams.length > 2 || !contentParams[1].equals("mp4")) {
			return false;
		}
		return true;
	}
}