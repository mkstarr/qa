/*
 *
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
 *  All Rights Reserved.
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
package com.codenvy.ide.core;

import com.codenvy.ide.IDE;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */
public class FactoryURL extends AbstractTestModule {
    public FactoryURL(IDE ide) {
        super(ide);
    }

    interface CreateAndPublishFactoryForm {
        String FACTORY_URL_FORM = "//div[@view-id='ide.factory.pupop']";

        String FACTORY_URL_FORM_FINISH_BTN_ID = FACTORY_URL_FORM + "//td[text()='Finish']";

        String FACE_BOOK_SHARE =
                FACTORY_URL_FORM + "//div/img[contains(@style, 'width:50px;height:50px;background:url(data:image/png;" +
                "base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAABXUlEQVR42u3avUvDQBgG8Pu" +
                "/kuDupKK94CyCg7OLILgKOrjZXECKiJOlCCKKFSsWVETEgp8gLhahk4ktUny9pFYU4oGVlLv6PPBAlsD74xJueRn7iJPxBmwudmWfZUnzBjb3i7YrBtnX" +
                "WBlv3oDhE+twsdA6CdcfNhXRruX6nMnjKZkOkZ9ZmcmH0HyIqLMeQMQFBBBAAEm//WM5yq4dU+WmSvXGK6miLWQxV6bwRT281pC+UZ92Dm/pt9EOkt+uUC" +
                "fRCjI1t0WdRivI3UPNfMjETIH+Em0gy+unykGbzbf4/1laPUqsNpDSyb0SEkGNuBDPLx+VkMnZDTMgF9dVJWR8Og8IIKquFM4S+1QLlJDN/asf343adUhaA" +
                "QQQQAABBDc7IIAAAggggAACyD+EhD0AaUSQA/Mh8QqHN2I6xOGe27U1p7Qgn2tO3xfP/GJrqUt7SGC7Ys/i2aH2/O9oP2dSaDAU7AAAAABJRU5ErkJg" +
                "gg==) no-repeat 0px 0px;')]";

        String GOOGLE_PLUS_SHARE =
                FACTORY_URL_FORM + "//div/img[contains(@style, 'width:50px;height:50px;background:url(data:image/png;ba" +
                "se64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAEG0lEQVR42u2a+U8TQRTH+1/RaqLGK9JCQIQEFBMSRSHRBA3GAw1eSBo54gFGRAKpQU" +
                "EkRomKCgJGwiUIQUPbbSltaUsLvVtOM+5b5Fi6Q7vbbZcSmrykP+ybzmdn5r3vex2R6P9HLY1LUkrF7SqZ2KOSitGWNnKOMFeYs2j9RxUvub/lJ48xtVR" +
                "cSkEo4yXJsQqxYsAgIr90xDoIMACIexuAuEXbAIKyHZAdkB0QjiBE2mE0fi4L6QsuIMPda2hSfpOzqY/uiy6IOmU/sjx9iPxqJfo76+fFbE31QSdheiCn" +
                "+WhPpXEEkUmoweYt5rUB/T40O6GloBbtM5wgpt82hTQJXkBgC7m6v9MAppsbkOZk4tqDCbuQvvAS8v0ewU560WFH85OGVZtpaaZeUNRA/KoxGoRRXoh3I" +
                "IFsjQpmEJcLmcqLOe1vXkDoW+F1SE62BgV2ZUylRcKCwBsljstCcyRXxvtrELsymqwU4UDgnLBx1J7JQEs+HyOM/VNLYN1w7ADS5Z1mNOvLWpq/4fYV7" +
                "LNBQSzVlazfgqOtFbsqG3OHviCPl3AeFGRSfos1yMTl89gfhCgnCAhkb/a1s4TMPSbGH5yqeSIMCJdoA+b60cWcDMlcRJM8ZCAxFt9gNMg5633NFWXYZ" +
                "4OCQEjlAmKtrwsrq/MetTw/ezmBgDZj3Fp1z4QBWfJ6kOZEAusBzBWlzIedVM2CZfYpRTXrAcBnIwQEAEiagoGAyqWJxRDM2dkWAGKpeiSs1gJz9/eG/" +
                "DYh6S1M22j+3tFhVqsRMRAwx+cPSJ24O3jEUjyn+c3pdeSKJrGeBJERT5Mh6qQ9/IBQUWywH2mz07GOhjtXqQCx8rxfo2ItFnktdT0Dvfhiye1CjvZW" +
                "KhGN52ai8ZwTlKBzdnylapfV7djXjYjUQwI3H8j9PFVbRQo9JyfJYP/4jvWZiGjzASQEAEE5i5PngduvL+RyVpB2EBy28ZxMNJGfSxmuq2IoKoidBh2" +
                "EQtyK6C6ejR0QaEZge1bNr2II5N71Tc8J185J1EEgwa3PF0wGNbfQh14UbvtnrXnRhYj0I1sPBHQUSAc47JDhPUMDQWHmjPpNOx2RByG3hS4/h3rzIE" +
                "0WrBbONTWoAS6NjLBBzJXlyK8lsP1cP6FE3uEhqiE3q9VQEw0KRMoX8+OS6IIEFEWTBmR9UUNukWxG6QHJEtpA7v6eoDCgy6IOsmCdWg6loeomcjtCm" +
                "bvodGBhYEwiQxo9EO/IEOvKcH3r1Kf8g0+ajYrogPjGRpfC/WtMnbyXKsaYQOZNxuhcGCDfaA8/N3YkyPntCyMMkXowwiBxnbxeqoHiiil0R7xylIl" +
                "Tlu9qycQlfA068/5NAAiXZkLIN4Ok4jLana3Vi2dSsTecgZkadhAQeAbwbrx49g+3jwy3Rdz/PQAAAABJRU5ErkJggg==) no-repeat 0px 0px;')]";

        String TWITTER_SHARE =
                FACTORY_URL_FORM + "//div/img[contains(@style, 'width:50px;height:50px;background:url(data:image/png" +
                ";base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAADQElEQVR42u2a2U8TQRzH98+igPrgkShq0GhIRE3UGB8UE6PGeDxYK5dGSk" +
                "TgxXCTABEJhBg0URF62LIVFlooyiktLYJiD6G0tD/nN7EGIke7nV1ps9/k1/ZlO/PZmfldGY77I5WmJ1v1QPeemI8Y7HDz0bmqe49xa6VS60qSYPKb" +
                "mZZCpGv0J5IYglqaWpfDpat1vckOQszAkY9fKQCyzKUABDUFRAFRQBQQ6Wx3sREqPkzDiMsHK6sRiEQAFnwr0GWbhzM1wobPnK4WgARvNiD3O7+QP0" +
                "sM4nzdEJl0ELZSu+CGjHwDZBYY4NqLETBNLMI7+wKbFcl6xtNBaowzoiHwbQfJCsQi52IAAqEw/Y3fh8t5NiCFr8f/DlL30RH3ymQWGmHOG4B4FQqH" +
                "4WrLMOx9YoLbbaOJg9SbHOsGwP28q8gYM0jRmhcRj2yzPmq4kleabImDNJic/wwyubAEp6oGYnqen/oJiaiga4zN1ire5I2ix2kbcMOBUvOWz3/3B0" +
                "UBhMIR0LwaY+d+j1ZYth2wQ5iDs7XChucnenDjVWXPNPs4Yhj/EdPgeKg7B+fodjhXOwhZZTyNE2KkfTvJHgRdoC8QAjmFToIpyOM3E3Cv4zPkNQ+D" +
                "d1k+mBsv7WxBqg2OdedBLuU8H2ALcpn4cLmFsQNTFaYg6Q/14PYEZAURZjzSZL/XW+2ygpR3T0mXxuOfy6Uj2ySKCdcjt9rsohLAeIQpjaSFFR4+TB" +
                "nuto/Cp68eyUDQzUteIfbPeCRdDavTK0+pm0sy3lWJYkmEZKG5MWbUTGp23F44KGthuSB78yGv2cb00A85vDEHQOZdFBz4JsmHWvtdMC8yw43W6NvV" +
                "NJK3g/Y8MkIz7xINgat6qIz/f32tfSUmqCCFj2c5KBrC7vbDfq1ZngbdhfohuNhgpXaHxI8qvYPGkHACngv9RKPZSXtXsnUaDz7ladeElZMSHJ5NO4" +
                "qybK3sSgu0WFzgFVElYtzBbuGlRuvO6f1m5OvphLDYwnYmehxspEW1tLIK34gH6yP5UlPfLM2asd+rdOMVEAVEAWECkjIXBnSpcYVD3XsyJS7VpMw1" +
                "pw0unvmTYPJkjvruNI3ueHT+vwFlK8O7nvrsBwAAAABJRU5ErkJggg==) no-repeat 0px 0px;')]";

        String SHARE_BY_EMAIL =
                FACTORY_URL_FORM + "//div/img[contains(@style, 'width:50px;height:50px;background:url(data:image/png" +
                ";base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAADJklEQVR42u2azUsicRjH/RPbw7IH9aCiaKZdwggRT0L4shIhQVBRh/BQKU" +
                "EXD+2asoYFgkQgFnlRKFMhta2snp3vj0bSNk2dcVTmCw/o+DLzmed1fvwUijepVKopzn5xVuOMRtyqb9c6pXgvtVrtH4OL/8x+MgilUvljjCGYcY74" +
                "DpA/4w7CWQy58XcCQOqKCYBgJoPIIDKIDKIijUZDJpNpJMxgMPQPotPp6PT0lKRWuVwmh8PRP4jf7yeLxUIHBwf0+voqCUQul6PZ2VlaWVnpHyQUCt" +
                "Ht7S0tLCzQ2toaNRqNoUKkUikWVoiKi4uLwUCger1OHo+HFhcX2ethKBKJsGi4urpi7wUBgZ6fn2ljY4Pm5+fp5uZGNACcZ2tri0VBsVhsHhcMhBfy" +
                "ZXp6mrLZrOAQ8LbP5yO32/3B84KDQIhZlMNEIiEYBO4+vLC+vs680i5RQPg/NpvNFA6HB65oyINu1VE0EAi5gpxZXV2lp6enviDgXaPRSCcnJx2/Jy" +
                "oIH9eIaZfLRXd3d18GwJ2HB+BVXGQ3iQ4Cob+gz8zNzVGhUPjy93upgEMBgarVKgsR2Pn5eUcPoh+p1eqexp+hgKDKeL1eWl5eZo1Mq9VSLBbrmFOA" +
                "nZmZoevr69EB2d7eZkPd/f09e392dsbK8+7ubrMKoe+g/+zv7zePxeNxBsb/TlKQaDTK7mypVGo5jlxBzgQCATo6Ovq07wSDQdYEX15epAPJZDKk1+" +
                "vp8vLyv5/XajWWD50mAQAABECSgCDecYHHx8dd86fdW+1CaCHEEGpDBcGJ7XY77e3tCTaiIOkRop95TnAQhMLS0hKrUN3iulehklmt1papVzSQnZ0d" +
                "VqEeHh5EGeMPDw9bKqAoIMgHuB/P0GJqc3Pzg8cFA8GEihLKP7GJKRQIVLv3OSgISKVSYZ5IJpNDe17HyIM+xJ9zYJDHx0dyOp09zV1CKZ/Ps+kYUT" +
                "AQCFyLZRjEq1TLQel0mi0HYcDsGwThhCkVXRdQUpnNZmPLQvLarwwig8ggMshIgkzMhoHEBIDEJ2dTzcRsc+LFeeYbd/D3mGw8q7VvPPsHdJ08PGx" +
                "irSkAAAAASUVORK5CYII=) no-repeat 0px 0px;')]";

        String NEXT_BUTTON = FACTORY_URL_FORM + "//td[text()='Next']";

        String FINISH_PAGE = FACTORY_URL_FORM + "//div[text()='Directly']/../../../../../div[contains(@style, 'left: 0%;')]";

        //codenow customize locators section---------------------------------------------------------------------------------------
        String SHOWN_NUMBER_OF_PROJECTS_CREATED_FROM_YOUR_FACTORY = "checkShowCounter";

        String CODENOW_WHITE_BUTTON_IMG_WITH_COUNTER_BASE64 =

                "url(data:image/png;base64," +
                "iVBORw0KGgoAAAANSUhEUgAAAE0AAAAVCAYAAAAD1GMqAAADOElEQVR42u2ZW09TQRDH6+VNfPMTiPEL" +
                "+BmskigaQcBSpFzkIiABVAxIAsolEgEFxEQlotF4I6jxhjHwgIqQKIgRw8UqCCgKtFSkIgTG8187x9PaQiliTTiTTHbPznSz+8vszJ5TjeaX+EjqK" +
                "+l6VV3qWklXaRTia7FYnpAqLsVsNtfZwAlZrtVqN6hY5hZbxAlZwdDa2t9QVsFx8g" +
                "+NpI0BOtoRHkP5JeX0vrdPJeYM2sVr1QKUX1AYZeYVUnHFWUrLOkrawFAxVtfwVIXmCA3A4vdn0OCXITvHLuM72rUngTZL4G7df0ifh4ZdTmrs6aXJyUmvb85qtVJHV7doFxXaFp2BhkdMTp3bOzpFFEIReacqL9D09PQffskZ2RQiAe7p6/d4YcMjI1T7qF5WPM9XAAxrRbuo0Hrn2GjfwEdqftFKKZk5YkEHs/Po5r1aGrVYZJ/vExO0aaeeys9VebSoD/39FBIVLzS/6KTcx/h/Cc3dH05NTVFi+mE58vz1kdTQ2Czbt4dFU2HZaY8WVXamUszJ0YUWz1WXr8o+LS/bRAQ+bmyyO37oc3TCRwlNaVsISI+hQWZmZuiHlLsanjVTcNReUSg63xqFLcAQIwqJJ8IR5hg1HGmABxiHpCjnlsHhdzyGeRga7Ep/tAD+z6HZbarbKBaSU1ginnOLSsURrbn7YN5zYR5HaMqjq4w6jiZEj6MNYwyN+/CHAFxcarp3oUEiktJIF5sk+sh7KBZbdRF/FRrnKd48IoihOdqUOY2hARbnSTx7HZo+bh8ZElPtAH4dG5v3PHy0nIFkEICgjDwlND52zqCxH6tXobW+ei0Wday0Qi4EBSfKPZqLjxwKAjbGhYGTPoDiaMGPbfDjggHojjYGCBtDZPBegdb0vIUCDbHkF7xbvptt00d5XD0hAAQw2Cha5QYRXZzMYVMmdPT5ilJ9+45d9ZxtzgVBwyuTu4q7WpAtNyCyAA8y8GlQjJ2/cn1pvEYhL7mr4QkplJp1hC7dqCHz6O/LbXTyAcotLqVv4+NL5yuHKu5DWybpOvUj5OxiMpnqbV+3ZVkDcOon7VnV18ZJlpW2/wlWq+pSfWycND8BuyIyAf2V3WEAAAAASUVORK5CYII=)";


        String CODENOW_DARK_BUTTON_IMG_WITH_COUNTER_BASE64 =
                "url(data:image/png;base64," +
                "iVBORw0KGgoAAAANSUhEUgAAAE0AAAAVCAYAAAAD1GMqAAADQUlEQVR42u2Y30tUURDHN/Ax6bm" +
                "/w4cehaxEQ3JTcVNLTE3LTFT8QRqIoUEWGFkPkWGCVFhYRGJZJppphZaWuSb9INcfua7amrsbq073e9q53d02Xa+5Ct6B4Zw9M/fsOR" +
                "/mzJx7dTpJgvfF6EP0B77tjoojTb3rTn3saHBEdJiOJURvmNLALK8hkYYxCVeALiho1zYNiO8qQdsqQ8s9dZo6X3WTzW4niHV2llraOyg1O1" +
                "+D5Q4tUECrvXVHgHLOz9OL7tfU+LiFevvf0+Liohg7U1mlAfOEBmBDnz5TQvoJN4djeUVktkzRvASu6moNxadl" +
                "/nOytJxCCjckboiNGZKPUvn5C6JdM2gOx08ypGZ4dco6WUIsiLyGB00UGpPwl1" +
                "+/cZAmJcApWXmrWlR6TgHdvndfVvxe6RwABkG7ZtCSl9loUmYuFZWdpbcDRrGYnr53dKn6OkUnpcs+EXFJtLCwQHcbH6peUGFJGdlsNqHGD0NyH" +
                "+MbDpqvD4TFHiLj0Ec58mw2O5Weq5TtP+bm6NHTNtUL6nnTK+bl6EILaX/eJftcvFItIrCm7qbb8UOfoxM" +
                "+SmhK22pBrhgadE90PO2VcldpRSVZpqdFocjILxa279ZZUUjULogjzDNqONIAD/LVZJJbBofneAzzMDTYlf4QAPcrNKUeLyj+HQldL8Xv1o5OcUQvX6tVNR" +
                "/EE5ry6CqjjqMJ0eNpwxhD4z78YQO4SYtl/aBBh0dGacI8KfrIeygWdofjv0PjPMWbRwQxNE" +
                "+bMqcxNMDiPAlZV2jjE2YyjY65AdyfeETVXHy0vIFkEICgjDwlND523qCxH+u6QctzLby5tV0uBE" +
                "/aOlQviI8cCgI2xoWBkz6A4mjBj23w44IB6J42BggbQ2TwfodWXF5BM1YrOZ1O" +
                "+W42J21qNdUTCkAAA0Gr3CCii5M5bMqEjj5fUZqaW9yq51JzqoaGVyZfFXe1qekZsQBEFuBhssSMbDFWV9+wOV6jkJd81ZGxcerrH6CaG/UUc" +
                "/jP5fbLsIlan3VS5MGUzQFNexFfITRJtmgfIX38CKk3jDM01+dug1kDsySwkR2h4VHiI6RCAlwDgX7W7S7193" +
                "+qeQ58An4BI5H6TO78gg0AAAAASUVORK5CYII=)";

        String CODENNOW_BUTTON_CSS = "div.codenow-bottom";

        String CODENOW_WHITE_BUTTON_CLASS_DIV = "div.codenow-white";

        String CODENOW_DARK_BUTTON_CLASS_DIV = "div.codenow-dark";

        String CODENOW_VERTICAL_BUTTON_CSS = "div.codenow-counter-vertical";

        String CODENOW_HORIZONTAL_BUTTON_CSS = "div.codenow-counter-horizontal";

        String CODENOW_DIV_XPATH = "//div[@class[contains(., 'codenow-counter')]]/span[text()='%s']";

        String COUNTER_NUM = "//div[@class[contains(., 'codenow-counter')]]/span[text()='%s']";

        String CODENOW_IFRAME = "//div[@view-id='ide.factory.pupop']//iframe[@style[contains(., '#fafafa')]]";

        String VERICAL_RADIO_BTN_ID = "radioVertical";

        String HORIZONTAL_RADIO_BTN_ID = "radioHorizontal";

        String DARK_RADIO_BTN_ID = "radioDark";

        String WHITE_RADIO_BTN_ID = "radioWhite";

        //********************Advenced options section Locators
        String ADWANCED_OPTIONS_IFRAME = "createFactoryIFrame";

        String ADWANCED_OPTIONS_MAIN_CONTAINER = "//iframe[@name='createFactoryIFrame']/following-sibling::div";

        String ADWANCED_OPTIONS_DROP_LABEL = "//div[text()='Advanced Options']";

        String UPLOAD_LOGO_INPUT_XPATH = "//span[text()='Upload image (JPG, GIF or PNG)']/parent::label/input";

        String LOGO_UPLOADED_IMG_CONTAINER = "//div[@class='advanced-factory-noted']/img";

        String LOGO_QUEST_MARK = ADWANCED_OPTIONS_MAIN_CONTAINER + "//form/following-sibling::div";

        String DESCRIPTION_QUEST_MARK = "//textarea/following-sibling::div";

        String CONTACT_EMAIL_QUEST_MARK = "//div[text()='Contact email:']/following-sibling::div";

        String AUTHOR_QUEST_MARK = "//div[text()='Author:']/following-sibling::div";

        String OPEN_AFTER_LAUNCH_QUESTION_MARK = "//div[text()='Open after launch:']/following-sibling::div";

        String ORGANIZATION_ID_QUESTION_MARK = "//div[text()='Organization ID:']/following-sibling::div";

        String AFFILIATE_ID_QUESTION_MARK = "//div[text()='Affiliate ID:']/following-sibling::div";

        String DESCRIPTION_TEXT_AREA = "//div[@id='ide.factory.pupop-window']//textarea";

        String CONTACT_EMAIL_FIELD = "//div[text()='Contact email:']/following-sibling::input";

        String AUTHOR_FIELD = "//div[text()='Author:']/following-sibling::input";

        String OPEN_AFTER_LAUNCH_FIELD = "//div[text()='Open after launch:']/following-sibling::input";

        String ORGANIZATION_ID_FIELD = "//div[text()='Organization ID:']/following-sibling::input";

        String AFFILIATE_ID_FIELD = "//div[text()='Affiliate ID:']/following-sibling::input";

        //*******************Advanced options section locators **********************


        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------

    }


    interface Locators {


        String ON_WEBSITES_URL = "//div[@view-id='ide.factory.pupop']//div[text()='on Websites']/following-sibling::div/textarea";

        String ON_GITHUB_PAGES_URL = "//div[@view-id='ide.factory.view']//div[text()='on GitHub Pages']/../textarea";

        String DIRECT_SHARING_URL = "//div/img[contains(@style, 'width:32px;height:32px;background:url(data:image/png;" +
                                    "base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACkUlEQVR42r2XzUtbQRTFoxBXDX" +
                                    "VhVl0Hsqi6l+p/UFMaW2xJLVXatR+7Jum22abWlYuKCz+gQu3S/gVaqAvRhcWduskmASWJhNgzcB4Ml3n" +
                                    "z5iXvdeAHIbyZc9/MvefOSyTiGUkwBAYS/3GkwXPwGWyD7+AreA+ycYsXwAFogntBB5yBIhiJWngQfAR1" +
                                    "g7CkCzZAJkrxEmgbhNR/Lf6WgWxEsROe+J1Y/BqsgtfgGZ85MgRY7EdcZXbZIH4MZgyZPw62xLNnvSamTT" +
                                    "xnmTcGDkVifuglADXpnG/wF9QcxL1RFDmxxqN0HspYnoI58A68JROO86dFme5xTacxBargFDS40C2Tbh/M" +
                                    "g1TAGrOsDC+AXTpm4JkvgQufkvJQZfcNPLasUxVrVFwTru1gMh5qN0YNa6nquNKeUzuY76XOW6zrH7TeK0" +
                                    "MQaiceaGvlmKj6MwfsHaHET8AKm4tKwofMjU3xnJq3YBFvsHeEEleLLIJ1bt8lmOScR4YgPoEXBvG7IBc" +
                                    "s+oi/5KL6/5sUTzCYSwa3zmD/GMRLttovGLqabjILhuBUEE/AMI9FGdUyjyuUeJqJYbPXFDuZTDr15r/A" +
                                    "T/Bb1LonXg5yvbxwqWuWjhyjLDXXsmxRPPBqVhHtsmqZNMadaAdcPi5oYoHiSdqi7mqzAXNSzIl9esENd" +
                                    "7DORrXKEnVuMnvCpaYd506wOc2xMb3h3KEwXW6QrTHsjSXHUqtpLfq8nz7f0YI45FnbxI99sr2nb4Es30" +
                                    "BfcIvXKdmgZqIW97ux3LPxlHiur8AXlmgok3EdIz5G02Vitg0BRibujQyD6DqYTJ0fJZGJ67ZcYk50DMJ" +
                                    "N2nYh7u+9LKtjjT6xww/PvO0yEccYoLEk41j8H9zkZXB9Clh3AAAAAElFTkSuQmCC) no-repeat 0" +
                                    "px 0px;')]/..//textarea";


        String LOGIN_BUTTON = "//div[@id='exoIDEToolbar']//div[text()='Login']";

        String CREATE_ACCOUNT_BUTTON = "//div[@id='exoIDEToolbar']//div[text()='Create free account']";

        String SEND_FACTORY_URLVIA_EMAIL_FORM = "//div[@view-id='ide.commitChanges.view']";

        String INPUT_EMAIL_IN_SEND_FACTORY_URL_VIA_EMAIL_FORM = "//input[@id='sendMail.field.message']";

        String SEND_BUTTON = "sendMail.button.send.id";

        // commit your changes form locators
        String COMMIT_YOUR_CHANGES_FORM = "ide.commitChanges.view-window";


        String COMMIT_YOUR_CHANGES_FORM_LABEL =
                "//div[@view-id='ide.commitChanges.view']//div[text()='Last changes have not been committed on your git repository (see " +
                "Output view for details). The current status of your project will be committed.']";

        String COMMIT_YOUR_CHANGES_TEXT_AREA = "commitChanges.field.description";


        String COMMIT_YOUR_CHANGES_BTN_ID = "commitChanges.button.continue";


        String WITHOUT_COMMITING_ID = "//a[@id='commitChanges.button.continue']";

        String WELCOME_IFRAME = "//div[@panel-id='information']//iframe";

        String WELCOME_PAGE_TEXT = "//div[@class='content' and contains(.,'%s')]";

        String WELCOME_TAB_TITLE = "//div[@id='information']/../../div[contains(@style, 'width: 245px')]//td[text()='%s']";

        String COPY_TO_MY_WORKSPACE_BUTTON = "//div[@id='exoIDEToolbar']//div[text()='Copy to my workspace']";

        String SEND_BUTTON_DISABLED = "//div[@id='sendMail.button.send.id' and @button-enabled='false']";

        String SEND_BUTTON_ENABLED = "//div[@id='sendMail.button.send.id' and @button-enabled='true']";


    }


    //***********advances options web elements//***********
    @FindBy(name = CreateAndPublishFactoryForm.ADWANCED_OPTIONS_IFRAME)
    private WebElement advancedOptionsIframe;


    @FindBy(xpath = CreateAndPublishFactoryForm.FACTORY_URL_FORM_FINISH_BTN_ID)
    private WebElement finishButton;

    @FindBy(xpath = CreateAndPublishFactoryForm.NEXT_BUTTON)
    private WebElement createFactoryBtn;

    @FindBy(id = CreateAndPublishFactoryForm.SHOWN_NUMBER_OF_PROJECTS_CREATED_FROM_YOUR_FACTORY)
    private WebElement showNumberOfProjectsCheckBox;

    @FindBy(xpath = CreateAndPublishFactoryForm.ADWANCED_OPTIONS_DROP_LABEL)
    private WebElement adwncedOptionsDropIcon;

    @FindBy(xpath = CreateAndPublishFactoryForm.UPLOAD_LOGO_INPUT_XPATH)
    private WebElement adwncedOptionsUploadLogo;

    @FindBy(xpath = CreateAndPublishFactoryForm.LOGO_UPLOADED_IMG_CONTAINER)
    private WebElement uploadImgContainer;

    @FindBy(xpath = CreateAndPublishFactoryForm.ADWANCED_OPTIONS_MAIN_CONTAINER)
    private WebElement advancedOptionsMainContainer;

    @FindBy(xpath = CreateAndPublishFactoryForm.LOGO_QUEST_MARK)
    private WebElement logoQuestMark;

    @FindBy(xpath = CreateAndPublishFactoryForm.DESCRIPTION_QUEST_MARK)
    private WebElement descriptionQuestMark;

    @FindBy(xpath = CreateAndPublishFactoryForm.DESCRIPTION_TEXT_AREA)
    private WebElement descriptionTextArea;

    @FindBy(xpath = CreateAndPublishFactoryForm.AUTHOR_FIELD)
    private WebElement authorField;

    @FindBy(xpath = CreateAndPublishFactoryForm.OPEN_AFTER_LAUNCH_FIELD)
    private WebElement openAfterLaunchField;

    @FindBy(xpath = CreateAndPublishFactoryForm.ORGANIZATION_ID_FIELD)
    private WebElement organizationIdField;

    @FindBy(xpath = CreateAndPublishFactoryForm.AFFILIATE_ID_FIELD)
    private WebElement affiliateIdField;

    @FindBy(xpath = CreateAndPublishFactoryForm.CONTACT_EMAIL_FIELD)
    private WebElement contactEmailField;

    @FindBy(xpath = CreateAndPublishFactoryForm.CONTACT_EMAIL_QUEST_MARK)
    private WebElement contactEmailQuestMark;

    @FindBy(xpath = CreateAndPublishFactoryForm.AUTHOR_QUEST_MARK)
    private WebElement authorQuestMark;

    @FindBy(xpath = CreateAndPublishFactoryForm.OPEN_AFTER_LAUNCH_QUESTION_MARK)
    private WebElement openAfterLaunchEmailQuestMark;

    @FindBy(xpath = CreateAndPublishFactoryForm.ORGANIZATION_ID_QUESTION_MARK)
    private WebElement orginizationIdQuestMark;

    @FindBy(xpath = CreateAndPublishFactoryForm.AFFILIATE_ID_QUESTION_MARK)
    private WebElement affiliateIdQuestMark;


    //---------------------
    @FindBy(xpath = CreateAndPublishFactoryForm.CODENOW_IFRAME)
    private WebElement codeNowIframe;

    @FindBy(css = CreateAndPublishFactoryForm.CODENNOW_BUTTON_CSS)
    private WebElement codeNowBtnWithoutStylePrefix;

    @FindBy(id = CreateAndPublishFactoryForm.DARK_RADIO_BTN_ID)
    private WebElement radioBtnDark;

    @FindBy(id = CreateAndPublishFactoryForm.WHITE_RADIO_BTN_ID)
    private WebElement radioBtnWhite;

    @FindBy(id = CreateAndPublishFactoryForm.HORIZONTAL_RADIO_BTN_ID)
    private WebElement radioBtnHorizontal;

    @FindBy(id = CreateAndPublishFactoryForm.VERICAL_RADIO_BTN_ID)
    private WebElement radioBtnVertical;

    @FindBy(xpath = Locators.ON_WEBSITES_URL)
    private WebElement onWebSiteURL;

    @FindBy(xpath = Locators.ON_GITHUB_PAGES_URL)
    private WebElement onGitHubPagesURL;

    @FindBy(xpath = Locators.DIRECT_SHARING_URL)
    private WebElement directSharingUrl;

    @FindBy(xpath = CreateAndPublishFactoryForm.FACE_BOOK_SHARE)
    private WebElement faceBookShareBtn;

    @FindBy(xpath = CreateAndPublishFactoryForm.GOOGLE_PLUS_SHARE)
    private WebElement googlePlusShareBtn;

    @FindBy(xpath = CreateAndPublishFactoryForm.TWITTER_SHARE)
    private WebElement twitterShareBtn;

    @FindBy(xpath = CreateAndPublishFactoryForm.SHARE_BY_EMAIL)
    private WebElement shareViaEmail;

    @FindBy(id = Locators.SEND_BUTTON)
    private WebElement sendButton;


    // commit your changes WebElements:
    @FindBy(id = Locators.COMMIT_YOUR_CHANGES_FORM)
    private WebElement commitYourChangesView;

    @FindBy(id = Locators.COMMIT_YOUR_CHANGES_TEXT_AREA)
    private WebElement commitYourChangesTextArea;

    @FindBy(id = Locators.COMMIT_YOUR_CHANGES_BTN_ID)
    private WebElement commitYourChangesOkBtn;

    @FindBy(xpath = Locators.COMMIT_YOUR_CHANGES_FORM_LABEL)
    private WebElement commitYourChangesLabel;

    @FindBy(xpath = Locators.WITHOUT_COMMITING_ID)
    private WebElement commitYourChangesWitoutBtn;

    @FindBy(xpath = Locators.LOGIN_BUTTON)
    private WebElement loginButton;

    @FindBy(xpath = Locators.CREATE_ACCOUNT_BUTTON)
    private WebElement createAccount;

    @FindBy(xpath = Locators.COPY_TO_MY_WORKSPACE_BUTTON)
    private WebElement copyToMyWorkspaceButton;

    @FindBy(xpath = Locators.SEND_BUTTON_ENABLED)
    private WebElement enabledSendMailBtn;

    @FindBy(xpath = Locators.SEND_BUTTON_DISABLED)
    private WebElement disabledSendMailBtn;


    /** wait factory url form opened */
    public void waitFactoryURLFormOpened() {
        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAndPublishFactoryForm.FACTORY_URL_FORM)));
    }
//---------advanced options

    /** wait appearance advanced options iframe */
    public void waitAdvancedOptionsIframe() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.presenceOfElementLocated(By.name(CreateAndPublishFactoryForm.ADWANCED_OPTIONS_IFRAME)));
    }

    /** wait advanced options iframe and select it */
    public void selectAdvancedOptionsIframe() {
        waitAdvancedOptionsIframe();
        driver().switchTo().frame(advancedOptionsIframe);
    }

    /**
     * click on advanced option drop icon for open
     * advanced options Factory URL menu
     */
    public void clickAdvancedOptionsDropIcon() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(adwncedOptionsDropIcon));
        adwncedOptionsDropIcon.click();
    }


    /**
     * click on advanced option drop icon for open
     * advanced options Factory URL menu
     */
    public void uploadLogoImage(String path) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(CreateAndPublishFactoryForm.UPLOAD_LOGO_INPUT_XPATH)));
        //    adwncedOptionsUploadLogo.clear();
        adwncedOptionsUploadLogo.sendKeys(path);
    }

    /**
     * type description of the factory URL
     *
     * @param descr
     *         your description text
     */
    public void typeDescription(String descr) {
        descriptionTextArea.clear();
        descriptionTextArea.sendKeys(descr);
    }

    /**
     * type description of the factory URL
     *
     * @param author
     *         your description text
     */
    public void typeToAuthorField(String author) {
        authorField.clear();
        authorField.sendKeys(author);
    }

    /** click on open after launch form */
    public void clickOnOpenAfterLaunch() {
        openAfterLaunchField.click();
    }

    /** type organization to organization field */
    public void typeToOrganizationField(String organization) {
        organizationIdField.clear();
        organizationIdField.sendKeys(organization);

    }


    /** type organization to organization field */
    public void typeToAffiliateField(String affiliateField) {
        affiliateIdField.clear();
        affiliateIdField.sendKeys(affiliateField);

    }


    /** type organization to contact email field */
    public void typeToContactEmailField(String affiliateField) {
        contactEmailField.clear();
        contactEmailField.sendKeys(affiliateField);

    }
    //------------------------

    /** click on codenow button (locator with independent style - work with any style white or white) */
    public void clickOnCodenowBtnIndependStyle() {
        selectCodeNowWithButtonsIframe();
        codeNowBtnWithoutStylePrefix.click();
        IDE().selectMainFrame();
    }

    /** wait factory url form closed */
    public void waitFactoryURLFormClosed() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CreateAndPublishFactoryForm.FACTORY_URL_FORM)));
    }

    /** wait check state of the show number of project checkbox */
    public void waitShowNumProjectChkBoxIsChecked() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.elementToBeSelected(showNumberOfProjectsCheckBox));
    }

    /** wait check state of the show number of project checkbox */
    public void waitShowNumProjectChkBoxIsUnChecked() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.not(ExpectedConditions.elementToBeSelected(showNumberOfProjectsCheckBox)));
    }

    /** wait disappear codenow button on form */
    public void waitCodenowButtonDisappear() {
        try {
            selectCodeNowWithButtonsIframe();
            new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath(CreateAndPublishFactoryForm.CODENOW_DIV_XPATH)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IDE().selectMainFrame();
        }
    }

//advanced form section---------------------------//---------------------------------------------

    /**
     * get url for web site sharing
     *
     * @return url for web site sharing
     */
    public String getOnWebSiteURL() {
        return onWebSiteURL.getAttribute("value");
    }

    /**
     * wait appearance counter of the app with specified number
     *
     * @param numOfPrj
     *         expected number of the app
     */
    public void waitSpecifiedNumberOfCounterThProject(final int numOfPrj) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {

                try {
                    selectCodeNowWithButtonsIframe();
                    return driver().findElement(By.xpath(String.format(CreateAndPublishFactoryForm.COUNTER_NUM, numOfPrj))).isDisplayed();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    IDE().selectMainFrame();
                }
            }
        });
    }

    /**
     * wait appearance base 64 image in
     *
     * @param base64Img
     *         image encoded to 6ase 64
     */
    public void waitUploadedImage(final String base64Img) {
        selectCodeNowWithButtonsIframe();

        new WebDriverWait(driver(), 15).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    System.out.println(
                            "........................................getted-BASE64-From-Browser" + uploadImgContainer.getAttribute("src"));
                    return uploadImgContainer.getAttribute("src").contains(base64Img);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    IDE().selectMainFrame();
                }
            }
        });
    }

    /**
     * wait while uploaded image will appear into logo upload form
     *
     * @param img
     *         name with type of the img
     */
    public void waitDownloadedImageNameInLogoField(String img) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[text()='Logo (100 x 100):']/parent::form/label/span[text()='" + img + "']")));
    }

    /**
     * get Base64 img from white style codenow button
     *
     * @return image in Base64 format
     */
    public String getWhiteStyleImage() {
        return driver().findElement(By.cssSelector(CreateAndPublishFactoryForm.CODENOW_WHITE_BUTTON_CLASS_DIV))
                .getCssValue("background-image");
    }


    //--------------------------------------------------quest-marks------------------------------------------------------

    /**
     * @param questMess
     *         mess from quest mark form
     */
    public void waitAppearQuestMarkTextFromLogo(final String questMess) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return logoQuestMark.getCssValue("opacity").equals("1") && logoQuestMark.getText().contains(questMess);
            }
        });
    }


    /**
     * @param questMess
     *         mess from description quest mark form
     */
    public void waitAppearQuestMarkTextFromDescription(final String questMess) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return descriptionQuestMark.getCssValue("opacity").equals("1") && descriptionQuestMark.getText().contains(questMess);
            }
        });
    }


    /**
     * @param questMess
     *         mess from autor quest mark form
     */
    public void waitAppearQuestMarkTextFromContactEmail(final String questMess) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return contactEmailQuestMark.getCssValue("opacity").equals("1") && contactEmailQuestMark.getText().contains(questMess);
            }
        });
    }


    /**
     * @param questMess
     *         mess from author quest mark form
     */
    public void waitAppearQuestMarkTextFromAuthor(final String questMess) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return authorQuestMark.getCssValue("opacity").equals("1") && authorQuestMark.getText().contains(questMess);
            }
        });
    }


    /**
     * @param questMess
     *         mess from open after launch quest mark form
     */
    public void waitAppearQuestMarkTextFromOpenAfterLaunch(final String questMess) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return openAfterLaunchEmailQuestMark.getCssValue("opacity").equals("1") &&
                       openAfterLaunchEmailQuestMark.getText().contains(questMess);
            }
        });
    }


    /**
     * @param questMess
     *         mess from open after launch quest mark form
     */
    public void waitAppearQuestMarkTextFromOrganizationId(final String questMess) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return orginizationIdQuestMark.getCssValue("opacity").equals("1") &&
                       orginizationIdQuestMark.getText().contains(questMess);
            }
        });
    }


    /**
     * @param questMess
     *         mess from open after launch quest mark form
     */
    public void waitAppearQuestMarkTextFromAffiliateIdQuestMark(final String questMess) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return affiliateIdQuestMark.getCssValue("opacity").equals("1") &&
                       affiliateIdQuestMark.getText().contains(questMess);
            }
        });
    }


    /** move mouse to 'Logo' quest Image into create and publish factory form */
    public void moveCursorToLogoQuestMark() {
        new Actions(driver()).moveToElement(logoQuestMark).build().perform();
    }


    /** move mouse to 'Description' quest Image into create and publish factory form */
    public void moveCursorToDescriptionQuestMark() {
        new Actions(driver()).moveToElement(descriptionQuestMark).build().perform();
    }


    /** move mouse to 'Author' quest Image into create and publish factory form */
    public void moveCursorToAuthorQuestMark() {
        new Actions(driver()).moveToElement(authorQuestMark).build().perform();
    }

    /** move mouse to 'Contact email' quest Image into create and publish factory form */
    public void moveCursorToContactEmailQuestMark() {
        new Actions(driver()).moveToElement(contactEmailQuestMark).build().perform();
    }


    /** move mouse to 'open after launch quest mark' quest Image into create and publish factory form */
    public void moveCursorToOpenAfterLaunchQuestMark() {
        new Actions(driver()).moveToElement(openAfterLaunchEmailQuestMark).build().perform();
    }

    /** move mouse to 'open after launch quest mark' quest Image into create and publish factory form */
    public void moveCursorToOrganizationIdQuestMark() {
        new Actions(driver()).moveToElement(orginizationIdQuestMark).build().perform();
    }


    /** move mouse to 'open affiliate quest mark' quest Image into create and publish factory form */
    public void moveCursorToAffiliateIdQuestMark() {
        new Actions(driver()).moveToElement(affiliateIdQuestMark).build().perform();
    }
    //--------------------------------------------------quest-marks-end----------------------------------------


    /** wait appearance white image for codenow button */
    public void waitWhiteStyleCodenwyImg() {
        new WebDriverWait(driver(), 5).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    selectCodeNowWithButtonsIframe();
                    return getWhiteStyleImage().equals(CreateAndPublishFactoryForm.CODENOW_WHITE_BUTTON_IMG_WITH_COUNTER_BASE64);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    IDE().selectMainFrame();
                }

            }
        });
    }

    /** wait codenow buton with dark image */
    public void waitDarkStyleCodenvyImg() {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    selectCodeNowWithButtonsIframe();
                    return driver().findElement(By.cssSelector(CreateAndPublishFactoryForm.CODENOW_DARK_BUTTON_CLASS_DIV))
                            .getCssValue("background-image").equals(
                                    CreateAndPublishFactoryForm.CODENOW_DARK_BUTTON_IMG_WITH_COUNTER_BASE64);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    IDE().selectMainFrame();
                }

            }
        });
    }

    /** wait appearance div with vertical style for 'codenow' button */
    public void waitVerticalStyleIsSetted() {
        try {
            selectCodeNowWithButtonsIframe();
            new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(CreateAndPublishFactoryForm.CODENOW_VERTICAL_BUTTON_CSS)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IDE().selectMainFrame();
        }
    }


    /** wait appearance div with vertical style for 'codenow' button */
    public void waitHorizontalStyleIsSetted() {
        try {
            selectCodeNowWithButtonsIframe();
            new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(CreateAndPublishFactoryForm.CODENOW_HORIZONTAL_BUTTON_CSS)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IDE().selectMainFrame();
        }
    }


    /** click on Show number of projects created from your factory check box */
    public void clickOnShowNumberOfProjectCheckBox() {
        showNumberOfProjectsCheckBox.click();
    }


    /** select i frame with codenow button */
    public void selectCodeNowWithButtonsIframe() {
        driver().switchTo().frame(codeNowIframe);
    }


    /** click on radio btn for setting dark style */
    public void clickRadioBtnDark() {
        radioBtnDark.click();
        new WebDriverWait(driver(), 10).until(ExpectedConditions.elementToBeSelected(radioBtnDark));
    }

    /** click on radio btn for setting white style */
    public void clickRadioBtnWhite() {
        radioBtnWhite.click();
        new WebDriverWait(driver(), 10).until(ExpectedConditions.elementToBeSelected(radioBtnWhite));
    }

    /** click on radio btn for setting wertical style */
    public void clickRadioBtnVertical() {
        radioBtnVertical.click();
        new WebDriverWait(driver(), 10).until(ExpectedConditions.elementToBeSelected(radioBtnVertical));
    }

    /** click on radio btn for setting horizontal style */
    public void clickRadioBtnHorizontal() {
        radioBtnHorizontal.click();
        new WebDriverWait(driver(), 10).until(ExpectedConditions.elementToBeSelected(radioBtnHorizontal));
    }


//----------------------------------end------------------------------

    /**
     * get url for github pages sharing
     *
     * @return url for github pages sharing
     */
    public String getOnGitHubPagesURL() {
        return onGitHubPagesURL.getAttribute("value");
    }

    /**
     * get direct sharing url
     *
     * @return direct sharing url
     */
    public String getDirectSharingURL() {
        return directSharingUrl.getAttribute("value");
    }

    /** click on share via facebook button */
    public void clickOnShareViaFaceBook() {
        faceBookShareBtn.click();
    }

    /** click on share via google plus button */
    public void clickOnShareViaGooglePlus() {
        googlePlusShareBtn.click();
    }

    /** click on share via twitter button */
    public void clickOnShareViaTwitter() {
        twitterShareBtn.click();
    }

    /** click on share via email button */
    public void clickOnShareViaEmail() {
        shareViaEmail.click();
    }

    /** wait send factory url via email form opened */
    public void waitSendFactoryURLViaEmailFormOpened() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.SEND_FACTORY_URLVIA_EMAIL_FORM)));
    }

    /** wait send factory url via email form opened closed */
    public void waitSendFactoryURLViaEmailFormClosed() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.SEND_FACTORY_URLVIA_EMAIL_FORM)));
    }

    /** wait while button for sending factory URL on mail will be disabled */
    public void waitSendMailBtnIsDisabled() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOf(disabledSendMailBtn));
    }

    /** wait while button for sending factory URL on mail will be enabled */
    public void waitSendMailBtnIsEnabled() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOf(enabledSendMailBtn));
    }

    /** wait of the iframe welcom info tab */
    public void waitWelcomeIframe() {
        new WebDriverWait(driver(), 240).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.WELCOME_IFRAME)));
    }


    /** wait of the iframe welcom info tab */
    public void waitWelcomeIframeAndGotoMainFrame() {
        new WebDriverWait(driver(), 180).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.WELCOME_IFRAME)));
        IDE().selectMainFrame();
    }


    /**
     * type email in send factory url via email form
     *
     * @param email
     *         user email
     */
    public void typeEmailInSendFactoryURLViaEmailForm(String email) {
        WebElement element = driver().findElement(By.xpath(Locators.INPUT_EMAIL_IN_SEND_FACTORY_URL_VIA_EMAIL_FORM));
        element.clear();
        element.sendKeys(email);
    }

    /** click send button */
    public void clickSendButton() {
        sendButton.click();
    }

    /** click on ok button in factory url form */
    public void clickOnFinishButtonInFactoryURLForm() {
        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitFinishButtonInFactoryURLForm();
        finishButton.click();
        waitFactoryURLFormClosed();
    }

    /** wait commit your changes form */
    public void waitCommitYourChangesForm() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(commitYourChangesView));
    }

    /** wait commit your changes form will disappear */
    public void waitCommitYourChangesFormClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.COMMIT_YOUR_CHANGES_FORM)));
    }

    /** type message to commit your changes form */
    public void typeCommitDescription(String decRip) {
        commitYourChangesTextArea.sendKeys(decRip);
    }

    /** type message to commit your changes form */
    public void clickOkBtnCommitYourChangesForm() {
        commitYourChangesOkBtn.click();
        waitCommitYourChangesFormClosed();
    }

    /** type message to commit your changes form */
    public void clickWithoutYourChangesBtn() {
        commitYourChangesWitoutBtn.click();
        waitCommitYourChangesFormClosed();
    }

    /** select iframe with welcome panel */
    public void selectWelcomeIframe() {
        WebElement frameWithMarkers = driver().findElement(By.xpath(Locators.WELCOME_IFRAME));
        driver().switchTo().frame(frameWithMarkers);
    }

    /**
     * wait contain text into user info panel
     * does not check all text
     *
     * @param content
     */
    public void waitTextFragmentInWelcomePanel(String content) {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.WELCOME_PAGE_TEXT, content))));
    }


    /**
     * wait contain text  in user info panel
     * check all text into user info panel
     *
     * @param expectedContent
     */
    public void waitContentIntoInformationPanel(final String expectedContent) {

        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    selectWelcomeIframe();
                    return driver().findElement(By.cssSelector("div.content")).getText().contains(expectedContent);
                } catch (NoSuchElementException e) {
                    return false;
                } finally {
                    IDE().selectMainFrame();
                }
            }
        });
    }


    /**
     * wait contain text  in user info panel
     * for user with orgid only
     * check all text into user info panel
     *
     * @param expectedContent
     */
    public void waitContentIntoInformationPanelWithOrgId(final String expectedContent) {

        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    selectWelcomeIframe();
                    System.out.println("***************:\n"+driver().findElement(By.cssSelector("div#content")).getText());
                    return driver().findElement(By.cssSelector("div#content")).getText().contains(expectedContent);
                } catch (NoSuchElementException e) {
                    return false;
                } finally {
                    IDE().selectMainFrame();
                }
            }
        });
    }

    /** wait welcome panel with title Welcome */

    public void waitWelcomePanelTitle(String title) {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.WELCOME_TAB_TITLE, title))));
    }

    /** wait for login button in temporary workspace */
    public void waitLoginButton() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.LOGIN_BUTTON)));

    }

    /** wait for create account button in temporary workspace */
    public void waitCreateAccountButton() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.CREATE_ACCOUNT_BUTTON)));
    }

    /** click on login button */
    public void clickOnLoginButton() {
        loginButton.click();
    }

    /** click on create account button */
    public void clickOnCreateAccountButton() {
        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createAccount.click();
    }

    /** wait for copy to my workspace button in temporary workspace */
    public void waitCopyToMyWorkspaceButton() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.COPY_TO_MY_WORKSPACE_BUTTON)));
    }

    /** click on copy to my workspace button */
    public void clickOnCopyToMyWorkspaceButton() {
        copyToMyWorkspaceButton.click();
    }

    /** wait for ok button */
    public void waitFinishButtonInFactoryURLForm() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .visibilityOfElementLocated(By.xpath(CreateAndPublishFactoryForm.FACTORY_URL_FORM_FINISH_BTN_ID)));
    }

    /** wait for finish createion page */
    public void waitForFinishCreationPage() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CreateAndPublishFactoryForm.FINISH_PAGE)));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** click on create factory button */
    public void clickOnNextFactoryButton() {
        createFactoryBtn.click();
    }

}
