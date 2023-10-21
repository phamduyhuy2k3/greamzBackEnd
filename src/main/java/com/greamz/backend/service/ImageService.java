package com.greamz.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.greamz.backend.common.BaseEntityService;
import com.greamz.backend.dto.FileUpload;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.model.Image;
import com.greamz.backend.repository.IImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ImageService extends BaseEntityService<Image, String, IImageRepository> {
    @Autowired
    private Cloudinary cloudinary;

    public ImageService(IImageRepository repository) {
        super(repository);
    }
    public List<Image> getImagesByProductId(Integer id) { return super.repository.findByGameModel_Appid(id); }

    public Optional<Map> uploadImage(FileUpload fileUpload) {
        if (fileUpload.getFile() == null || fileUpload.getFile().isEmpty())
            return Optional.empty();
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(fileUpload.getFile().getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
        } catch (IOException e) {
            return Optional.empty();
        }
        fileUpload.setPublicId((String) uploadResult.get("public_id"));
        Object version = uploadResult.get("version");
        if (version instanceof Integer) {

            fileUpload.setVersion(Long.valueOf((Integer) version));
        } else {
            fileUpload.setVersion((Long) version);
        }

        fileUpload.setSignature((String) uploadResult.get("signature"));
        fileUpload.setFormat((String) uploadResult.get("format"));
        fileUpload.setResourceType((String) uploadResult.get("resource_type"));
        return Optional.of(uploadResult);
    }

    public Optional<Image> saveUploadedImage(FileUpload fileUpload, Map uploadResult, GameModel product) {
        Image image = new Image();
        image.setId(fileUpload.getPublicId());
        image.setLink((String) uploadResult.get("url"));
        image.setTitle(fileUpload.getTitle());
        image.setGameModel(product);
        super.repository.save(image);

        return Optional.of(image);
    }
}
