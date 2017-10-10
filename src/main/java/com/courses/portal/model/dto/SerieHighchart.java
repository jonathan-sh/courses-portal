package com.courses.portal.model.dto;

import com.courses.portal.model.dto.DataHighchart;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class SerieHighchart {
    @Expose
    public String name;
    @Expose
    public List<DataHighchart> data = new ArrayList<DataHighchart>();
}
