/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;


import java.util.Properties;

/**
 * @author Pesegato
 */
public interface BigBananaPeel {

    public String getFilePath();
    public Properties getProperties();
    public String getDefaultKeyBind(String key);
    public String getDefaultPadBind(String key);

}
