/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.properties.project;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;

/**
 * @author Musienko Maxim
 *
 */
public class PropertiesService extends BaseTest {

    protected void checkPaaSStatesAfterCloneProject() {
        // check AppFog
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG, MenuCommands.PaaS.AppFog.APLICATIONS);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG, MenuCommands.PaaS.AppFog.SWITCH_ACCOUNT);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG,
                                       MenuCommands.PaaS.AppFog.CREATE_APPLICATION);
        // check CloudBeeS
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.CloudBees.CLOUD_BEES,
                                       MenuCommands.PaaS.CloudBees.APLICATIONS);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.CloudBees.CLOUD_BEES,
                                       MenuCommands.PaaS.CloudBees.SWITCH_ACCOUNT);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.CloudBees.CLOUD_BEES,
                                       MenuCommands.PaaS.CloudBees.CREATE_ACCOUNT);
        IDE.MENU.waitSubCommandDisabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.CloudBees.CLOUD_BEES,

//TODO on this moment CloudFoundry does not work in the IDE
                                        MenuCommands.PaaS.CloudBees.CREATE_APPLICATION);
//        // check CloudFoundry
//        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.CloudFoundry.CLOUDFOUNDRY,
//                                       MenuCommands.PaaS.CloudFoundry.APPPLICATIONS);
//        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.CloudFoundry.CLOUDFOUNDRY,
//                                       MenuCommands.PaaS.CloudFoundry.SWITCH_ACCOUNT);
//        IDE.MENU.waitSubCommandDisabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.CloudFoundry.CLOUDFOUNDRY,
//                                        MenuCommands.PaaS.CloudFoundry.CREATE_APPLICATION);
        // check Elastic Beanstalk
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                                       MenuCommands.PaaS.ElasticBeanstalk.MANAGE_APPLICATION);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                                       MenuCommands.PaaS.ElasticBeanstalk.SWITCH_ACCOUNT);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                                       MenuCommands.PaaS.ElasticBeanstalk.EC2_MANAGEMENT);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                                       MenuCommands.PaaS.ElasticBeanstalk.EC3_MANAGEMENT);
        IDE.MENU.waitSubCommandDisabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                                        MenuCommands.PaaS.ElasticBeanstalk.CREATE_APPLICATION);
        // check Elastic GOOGLE_APP_ENGINE
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE,
                                       MenuCommands.PaaS.GoogleAppEngine.UPDATE_APPLICATION);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE,
                                       MenuCommands.PaaS.GoogleAppEngine.CREATE_APPLICATION);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE,
                                       MenuCommands.PaaS.GoogleAppEngine.LOGIN);
        // check OpenShift
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT,
                                       MenuCommands.PaaS.OpenShift.APPLICATIONS);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT,
                                       MenuCommands.PaaS.OpenShift.UPDATE_SSH_PUBLIC_KEY);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT,
                                       MenuCommands.PaaS.OpenShift.SWITCH_ACCOUNT);

        // TODO on this moment Tier3WebFabric does not work in the IDE
        // check Tier3WebFabric
//        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.Tier3WebFabric.TIRE_FABRIC_3,
//                                       MenuCommands.PaaS.Tier3WebFabric.APPLICATIONS);
//        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.Tier3WebFabric.TIRE_FABRIC_3,
//                                       MenuCommands.PaaS.Tier3WebFabric.SWITCH_ACCOUNT);
//        IDE.MENU.waitSubCommandDisabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.Tier3WebFabric.TIRE_FABRIC_3,
//                                        MenuCommands.PaaS.Tier3WebFabric.CREATE_APPLICATION);
    }


}
