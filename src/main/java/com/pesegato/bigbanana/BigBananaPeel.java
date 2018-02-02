/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import java.io.InputStream;

/**
 *
 * @author Pesegato
 */
public interface BigBananaPeel {

    public InputStream getDefaultConfigFile(String fileName);

    public String getDefaultBind(String key);

    public void loadDefaults();
}
