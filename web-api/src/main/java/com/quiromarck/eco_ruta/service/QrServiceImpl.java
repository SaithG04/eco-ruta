package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.entity.Qr;
import com.quiromarck.eco_ruta.exception.QrNotFoundException;
import com.quiromarck.eco_ruta.repository.QrRepository;
import org.springframework.stereotype.Service;

@Service
public class QrServiceImpl implements QrService {

    private final QrRepository qrRepository;

    public QrServiceImpl(QrRepository qrRepository) {
        this.qrRepository = qrRepository;
    }

    @Override
    public Qr getByLocationId(Long locationId) {
        return qrRepository.findByLocationId(locationId).orElseThrow(() -> new QrNotFoundException("Qr not found"));
    }
}
