package com.quanlytrungtam.controller;

import java.util.List;

import com.quanlytrungtam.dao.HoanTienDAO;
import com.quanlytrungtam.model.HoanTienModel;

public class HoanTienController {

    private final HoanTienDAO hoanTienDAO = new HoanTienDAO();

    public List<HoanTienModel> getListYeuCau() {
        return hoanTienDAO.getListYeuCau();
    }

    public boolean updateProcess(HoanTienModel ht) {
        return hoanTienDAO.updateProcess(ht);
    }
}
