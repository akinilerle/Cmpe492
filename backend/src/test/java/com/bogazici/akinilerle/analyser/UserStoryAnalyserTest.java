package com.bogazici.akinilerle.analyser;

import com.bogazici.akinilerle.model.response.Report;
import com.bogazici.akinilerle.model.UserStory;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;

import static java.util.regex.Pattern.matches;
import static org.junit.Assert.*;

public class UserStoryAnalyserTest {

    UserStoryAnalyser userStoryAnalyser = new UserStoryAnalyser();

    @Test
    public void it_should_find_errors_in_misspelled_user_stories() throws Exception {
        UserStory userStory = new UserStory("sisteme kayıtlı bir üye olarak"
                ,"kredi kartı ya da debit kart ile 2 ödeme seçeneğimin olmasını istiyorum"
                ,"böylece etkinlik bileti için sadece elden nakit ile ödeme yapmak zorunda kalmayacağım"
        ,UserStory.Type.TYPE_RRB);
        userStory.setOriginalString("");

        Report report = userStoryAnalyser.analyseSentence(userStory);
        assertTrue(report.getMessages().toString().matches("\\[Yazım hatası: \"debit\"\\. Bunu mu demek istediniz\\? : \\[.*]]"));

        userStory = new UserStory("etkinlik düzenlmek isteyen bir kuruluş olarak"
                ,"farklı bir arayuz kullanmak istiyoruz"
                ,"böylece gerçekleşcek etkinlikleri görmekten çok etkinliğimiz" +
                " için kiralayabileceğimiz uygun yerleri görmüş olacağız"
                ,UserStory.Type.TYPE_RRB);
        userStory.setOriginalString("");
        report = userStoryAnalyser.analyseSentence(userStory);
        assertTrue(report.getMessages().toString().matches("\\[Yazım hatası:.*]"));

        userStory = new UserStory("son kullanıcı olarak"
                ,"hesap etkinliğime ilişkin bir rapor almak istiorum"
                ,"her şeyin yolunda olduğunu kontrol etmekiçin"
                ,UserStory.Type.TYPE_RBR);
        userStory.setOriginalString("");
        report = userStoryAnalyser.analyseSentence(userStory);
        assertTrue(report.getMessages().toString().matches("\\[Yazım hatası.*]"));
    }

    @Test
    public void it_should_find_errors_in_user_stories_with_additional_notes() throws Exception {
        UserStory userStory = new UserStory("sisteme kayıtlı bir üye kullanıcı olarak"
                ,"kredi kartı ile 2 ödeme seçeneğimin olmasını istiyorum"
                ,"böylece etkinlik bileti için sadece elden nakit ile ödeme yapmak zorunda kalmayacağım"
                ,UserStory.Type.TYPE_RRB);
        userStory.setOriginalString("sisteme kayıtlı bir üye (kullanıcı) olarak + kredi kartı ile 2 ödeme seçeneğimin olmasını istiyorum " +
                "böylece etkinlik bileti için sadece elden nakit ile ödeme yapmak zorunda kalmayacağım");

        Report report = userStoryAnalyser.analyseSentence(userStory);
        assertTrue(report.getMessages().size() == 1);
        assertEquals("Kullanıcı hikayesi minimal olmalı, parantez içinde ekstra açıklama içermemelidir.", report.getMessages().get(0));

        userStory = new UserStory("etkinlik düzenlemek isteyen bir kuruluş olarak"
                ,"farklı bir arayüz kullanmak istiyoruz"
                ,"böylece gerçekleşecek etkinlikleri görmekten çok etkinliğimiz" +
                " için kiralayabileceğimiz uygun yerleri görmüş olacağız"
                ,UserStory.Type.TYPE_RRB);
        userStory.setOriginalString("etkinlik düzenlemek isteyen bir kuruluş olarak farklı bir arayüz kullanmak istiyoruz" +
                " böylece gerçekleşcek etkinlikleri görmekten çok etkinliğimiz için kiralayabileceğimiz" +
                " (uygun) yerleri görmüş olacağız");

        report = userStoryAnalyser.analyseSentence(userStory);
        assertTrue(report.getMessages().size() == 1);
        assertEquals("Kullanıcı hikayesi minimal olmalı, parantez içinde ekstra açıklama içermemelidir.", report.getMessages().get(0));

        userStory = new UserStory("son kullanıcı olarak"
                ,"hesap etkinliğime ilişkin bir rapor almak istiyorum"
                ,"her şeyin yolunda olduğunu kontrol etmek için"
                ,UserStory.Type.TYPE_RBR);
        userStory.setOriginalString("son kullanıcı olarak hesap etkinliğime ilişkin bir rapor almak istiorum, " +
                "her şeyin yolunda olduğunu kontrol etmek için");

        report = userStoryAnalyser.analyseSentence(userStory);
        assertTrue(report.getMessages().size() == 0);
    }

    @Test
    public void it_should_not_find_misspellings() throws Exception {
        UserStory userStory = new UserStory("etkinlik düzenlemek isteyen bir kuruluş olarak"
                ,"farklı bir arayüz kullanmak istiyoruz"
                ,"böylece gerçekleşecek etkinlikleri görmekten çok etkinliğimiz" +
                " için kiralayabileceğimiz uygun yerleri görmüş olacağız"
                ,UserStory.Type.TYPE_RRB);
        userStory.setOriginalString("");
        Report report = userStoryAnalyser.analyseSentence(userStory);
        assertEquals(Report.Type.OK,report.getType());

        userStory = new UserStory("son kullanıcı olarak"
                ,"hesap etkinliğime ilişkin bir rapor almak istiyorum"
                ,"her şeyin yolunda olduğunu kontrol etmek için"
                ,UserStory.Type.TYPE_RBR);
        userStory.setOriginalString("");
        report = userStoryAnalyser.analyseSentence(userStory);
        assertEquals(Report.Type.OK,report.getType());

        userStory = new UserStory("katılımcı olarak"
                ,"otomatlarda banka kartı kullanabilmek istiyorum"
                ,null
                ,UserStory.Type.TYPE_RR);
        userStory.setOriginalString("");
        report = userStoryAnalyser.analyseSentence(userStory);
        assertEquals(Report.Type.OK,report.getType());
    }

    @Test
    public void it_should_find_misstructured_sentences() throws Exception {
        UserStory userStory = new UserStory("etkinlik olsun konser olsun düzenlemek isteyen bir kuruluş olarak"
                ,"farklı bir arayüz kullanmak istiyoruz"
                ,"böylece gerçekleşecek etkinlikleri görmekten çok etkinliğimiz" +
                " için kiralayabileceğimiz uygun yerleri görmüş olacağız"
                ,UserStory.Type.TYPE_RRB);
        userStory.setOriginalString("");
        Report report = userStoryAnalyser.analyseSentence(userStory);
        assertNotNull(report);
        assertEquals(1, report.getMessages().size());
        assertEquals("Kullanıcı hikayesinin rol kısmı yüklem içermemelidir. Yüklemler: [olsun, olsun]"
                , report.getMessages().get(0));

        userStory = new UserStory("son kullanıcı olarak"
                ,"hesap etkinliğime ilişkin bir rapor almak istiyorum"
                ,"her şeyin yolunda olduğunu kontrol edeceğim için"
                ,UserStory.Type.TYPE_RBR);
        userStory.setOriginalString("");
        report = userStoryAnalyser.analyseSentence(userStory);
        assertNotNull(report);
        assertEquals(1, report.getMessages().size());
        assertEquals("Kullanıcı hikayesinin fayda kısmı yüklem içermemelidir. Yüklemler: [edeceğim]"
                , report.getMessages().get(0));

        userStory = new UserStory("son kullanıcı olarak"
                ,"hesap etkinliğime ilişkin bir rapor almak istiyorum"
                ,"böylece her şeyin kontrolü"
                ,UserStory.Type.TYPE_RRB);
        userStory.setOriginalString("");
        report = userStoryAnalyser.analyseSentence(userStory);
        assertNotNull(report);
        assertEquals(1, report.getMessages().size());
        assertEquals("Kullanıcı hikayesinin fayda kısmı yüklemli bir cümle olmalıdır: \"böylece her şeyin kontrolü\""
                , report.getMessages().get(0));
    }



}