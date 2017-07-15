package com.courses.portal.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created by jonathan on 7/11/17.
 */

@RestController
@RequestMapping(value = "/jujuba")
public class Teste {

    @RequestMapping(method = RequestMethod.GET)
    public LocalDateTime mimimi()
    {
        return LocalDateTime.now();
    }
}
