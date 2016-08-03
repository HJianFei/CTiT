/*******************************************************************************
 * Copyright (c) 1999, 2014 IBM Corp.
 * <p/>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * <p/>
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */
package com.hjf.ctit.constants;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * This Class provides constants used for returning results from an activity
 */
public class ActivityConstants {

    /**
     * Application TAG for logs where class name is not used
     */
    public static final String TAG = "MQTT Android";

  /*Default values **/

    /**
     * Default QOS value
     */
    public static final int defaultQos = 2;
    /**
     * Default timeout
     */
    public static final int defaultTimeOut = 5000;
    /**
     * Default keep alive value
     */
    public static final int defaultKeepAlive = 20;
    /**
     * Default SSL enabled flag
     */
    public static final boolean defaultSsl = false;
    /**
     * Default message retained flag
     */
    public static final boolean defaultRetained = true;
    /**
     * Default last will message
     */
    public static final MqttMessage defaultLastWill = null;
    /**
     * Default port
     */
    public static final int defaultPort = 1883;

    /**
     * Connect Request Code
     */
    public static final int connect = 0;
    /**
     * Advanced Connect Request Code
     **/
    public static final int advancedConnect = 1;
    /**
     * Last will Request Code
     **/
    public static final int lastWill = 2;
    /**
     * Show History Request Code
     **/
    public static final int showHistory = 3;

  /* Bundle Keys */

    /**
     * Server Bundle Key
     **/
    public static final String server = "server";
    /**
     * Port Bundle Key
     **/
    public static final String port = "port";
    /**
     * ClientID Bundle Key
     **/
    public static final String clientId = "clientId";
    /**
     * Topic Bundle Key
     **/
    public static final String topic = "topic";
    /**
     * Message Bundle Key
     **/
    public static final String message = "message";
    /**
     * Retained Flag Bundle Key
     **/
    public static final String retained = "retained";
    /**
     * QOS Value Bundle Key
     **/
    public static final String qos = "qos";
    /**
     * Action Bundle Key
     **/
    public static final String action = "action";

  /* Property names */

    public static final String historyProperty = "history";

    public static final String ConnectionStatusProperty = "connectionStatus";

  /* Useful constants*/

    /**
     * Empty String for comparisons
     **/
    public static final String empty = new String();

    public static final int REQCODE = 001;

    public static String em = "";

    public static String cleanSession = "false";
    /**
     * 被盗信息链接
     */
    public static String STEAL_INFO_IP = "http://192.168.1.108:8080/cargosys/app/baseInfo/getWarnInfo";
    /**
     * 被盗货物详情信息
     */
    public static String STEAL_INFO_DETAIL = "http://192.168.1.108:8080/cargosys/app/baseInfo/getBaseInfoByBarcode?barcode=";
}
