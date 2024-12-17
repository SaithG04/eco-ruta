package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.entity.Qr;

public interface QrService {
    Qr getByLocationId(Long locationId);
}
