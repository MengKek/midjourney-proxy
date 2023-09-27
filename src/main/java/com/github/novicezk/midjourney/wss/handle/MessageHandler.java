package com.github.novicezk.midjourney.wss.handle;

import cn.hutool.core.text.CharSequenceUtil;
import com.github.novicezk.midjourney.Constants;
import com.github.novicezk.midjourney.enums.MessageType;
import com.github.novicezk.midjourney.request.AttachmentRequest;
import com.github.novicezk.midjourney.service.CosService;
import com.github.novicezk.midjourney.support.DiscordHelper;
import com.github.novicezk.midjourney.support.Task;
import com.github.novicezk.midjourney.support.TaskQueueHelper;
import com.github.novicezk.midjourney.util.FileUtil;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import javax.annotation.Resource;
import java.io.File;
import java.net.URI;
import java.net.URL;

@Log4j2
public abstract class MessageHandler {
	@Resource
	protected TaskQueueHelper taskQueueHelper;
	@Resource
	protected DiscordHelper discordHelper;
	@Resource
	private FileUtil fileUtil;
	@Resource
	private CosService cosService;

	public abstract void handle(MessageType messageType, DataObject message);

	public abstract void handle(MessageType messageType, Message message);

	private static final String LOCAL_URL = "~";

	protected String getMessageContent(DataObject message) {
		return message.hasKey("content") ? message.getString("content") : "";
	}

	protected void finishTask(Task task, DataObject message) {
		task.setProperty(Constants.TASK_PROPERTY_MESSAGE_ID, message.getString("id"));
		task.setProperty(Constants.TASK_PROPERTY_FLAGS, message.getInt("flags", 0));
		DataArray attachments = message.getArray("attachments");
		if (!attachments.isEmpty()) {
			String imageUrl = attachments.getObject(0).getString("url");
			task.setImageUrl(replaceCdnUrl(imageUrl));
			task.setProperty(Constants.TASK_PROPERTY_MESSAGE_HASH, getMessageHash(imageUrl));
			task.success();
		} else {
			task.fail("关联图片不存在");
		}
	}

	protected void finishTask(Task task, Message message) {
		task.setProperty(Constants.TASK_PROPERTY_MESSAGE_ID, message.getId());
		task.setProperty(Constants.TASK_PROPERTY_FLAGS, (int) message.getFlagsRaw());
		if (!message.getAttachments().isEmpty()) {
			String imageUrl = message.getAttachments().get(0).getUrl();
			task.setImageUrl(replaceCdnUrl(imageUrl));
			task.setProperty(Constants.TASK_PROPERTY_MESSAGE_HASH, getMessageHash(imageUrl));
			task.success();
		} else {
			task.fail("关联图片不存在");
		}
	}

	protected String getMessageHash(String imageUrl) {
		int hashStartIndex = imageUrl.lastIndexOf("_");
		return CharSequenceUtil.subBefore(imageUrl.substring(hashStartIndex + 1), ".", true);
	}

	protected String getImageUrl(DataObject message) {
		DataArray attachments = message.getArray("attachments");
		if (!attachments.isEmpty()) {
			String imageUrl = attachments.getObject(0).getString("url");
			return replaceCdnUrl(imageUrl);
		}
		return null;
	}

	protected String getImageUrl(Message message) {
		if (!message.getAttachments().isEmpty()) {
			String imageUrl = message.getAttachments().get(0).getUrl();
			return replaceCdnUrl(imageUrl);
		}
		return null;
	}

	public String trimUrl(String urlWithParams) {
		URI uri = null;
		try {
			URL url = new URL(urlWithParams);
			uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uri.toString();
	}

	protected String replaceCdnUrl(String imageUrl) {
		String fileOldUrl = trimUrl(imageUrl);
		String result = null;
		try {
			String localFileAddr = fileUtil.downloadFile(fileOldUrl, LOCAL_URL);
			result = "http://43.131.246.99:8082/" + "/" + localFileAddr;
			return result;
		}catch (Exception e){
			log.error("转化文件地址报错", e);
			System.out.println(e);
		}
		return result;
	}

}
