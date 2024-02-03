package com.example.storeserver.services;

import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.ImageProfile;
import com.example.storeserver.repositories.CustomerRepository;
import com.example.storeserver.repositories.ImageProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * api/ImageProfile/upload - POST upload image to Product
 * api/ImageProfile/:productId/image – GET image to Product
 */

@Service
public class ImageProfileService {
    public static final Logger LOG = LoggerFactory.getLogger(ImageProfileService.class);

    private final ImageProfileRepository imageProfileRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public ImageProfileService(ImageProfileRepository imageProfileRepository, CustomerRepository customerRepository) {
        this.imageProfileRepository = imageProfileRepository;
        this.customerRepository = customerRepository;
    }

    // api/ImageProfile/upload
    public ImageProfile uploadImageProfileCurrentCustomer(MultipartFile file, Principal principal) throws IOException {
        Customer customer = getCustomerByPrincipal(principal);

        ImageProfile imageProfile = imageProfileRepository.findByCustomerId(customer.getId()).orElse(null);
        if (imageProfile == null) {
            imageProfile = new ImageProfile();
            imageProfile.setCustomerId(customer.getId());
        }

        imageProfile.setImage(compressBytes(file.getBytes()));

        LOG.info("Saving imageProfile");
        return imageProfileRepository.save(imageProfile);
    }

    // api/ImageProfile/image
    public ImageProfile getImageProfileCurrentCustomer(Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        ImageProfile imageProfile = imageProfileRepository.findByCustomerId(customer.getId())
                .orElse(null);
        if (!ObjectUtils.isEmpty(imageProfile)) {
            imageProfile.setImage(decompressBytes(imageProfile.getImage()));
        }
        return imageProfile;
    }

    // сжатие данных изображения
    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    // распаковка данных изображения
    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

    private Customer getCustomerByPrincipal(Principal principal) {
        String username = principal.getName();
        return customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }
}
