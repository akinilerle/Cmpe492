package com.bogazici.akinilerle.analyser;

import com.bogazici.akinilerle.model.Report;
import com.bogazici.akinilerle.model.UserStory;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class UserStoryAnalyserTest {

    UserStoryAnalyser userStoryAnalyser = new UserStoryAnalyser();

    @Test
    public void it_should_find_errors_in_valid_user_stories() throws Exception {
        UserStory userStory = new UserStory("sisteme kayıtlı bir üye olarak"
                ,"kredi kartı ya da debit kart ile 2 ödeme seçeneğimin olmasını istiyorum"
                ,"böylece etkinlik bileti için sadece elden nakit ile ödeme yapmak zorunda kalmayacağım"
        ,UserStory.Type.TYPE_RRB);

        Report report = userStoryAnalyser.analyseSentence(userStory);
        assertEquals(Arrays.asList("Yazım hatası: \"debit\". Bunu mu demek istediniz? : [debi, devit, debil]")
                , report.getMessages());

        userStory = new UserStory("etkinlik düzenlmek isteyen bir kuruluş olarak"
                ,"farklı bir arayuz kullanmak istiyoruz"
                ,"böylece gerçekleşcek etkinlikleri görmekten çok etkinliğimiz" +
                " için kiralayabileceğimiz uygun yerleri görmüş olacağız"
                ,UserStory.Type.TYPE_RRB);
        report = userStoryAnalyser.analyseSentence(userStory);
        assertEquals(Arrays.asList("Yazım hatası: \"düzenlmek\". Bunu mu demek istediniz? : [düzenlemek, düzenmek, düzenilmek]"
                ,"Yazım hatası: \"arayuz\". Bunu mu demek istediniz? : [arayüz, arayız, Ar'ayız]"
                ,"Yazım hatası: \"gerçekleşcek\". Bunu mu demek istediniz? : [gerçekleşecek, Gerçek'leşecek, gerçekleşmek]")
                , report.getMessages());

        userStory = new UserStory("son kullanıcı olarak"
                ,"hesap etkinliğime ilişkin bir rapor almak istiorum"
                ,"her şeyin yolunda olduğunu kontrol etmekiçin"
                ,UserStory.Type.TYPE_RBR);
        report = userStoryAnalyser.analyseSentence(userStory);
        assertEquals(Arrays.asList("Yazım hatası: \"istiorum\". Bunu mu demek istediniz? : [istiyorum, istorum]"
                ,"Yazım hatası: \"etmekiçin\". Bunu mu demek istediniz? : []")
                , report.getMessages());
    }

    @Test
    public void it_should_not_find_misspellings() throws Exception {
        UserStory userStory = new UserStory("etkinlik düzenlemek isteyen bir kuruluş olarak"
                ,"farklı bir arayüz kullanmak istiyoruz"
                ,"böylece gerçekleşecek etkinlikleri görmekten çok etkinliğimiz" +
                " için kiralayabileceğimiz uygun yerleri görmüş olacağız"
                ,UserStory.Type.TYPE_RRB);
        Report report = userStoryAnalyser.analyseSentence(userStory);
        assertEquals(Report.Type.OK,report.getType());

        userStory = new UserStory("son kullanıcı olarak"
                ,"hesap etkinliğime ilişkin bir rapor almak istiyorum"
                ,"her şeyin yolunda olduğunu kontrol etmek için"
                ,UserStory.Type.TYPE_RBR);
        report = userStoryAnalyser.analyseSentence(userStory);
        assertEquals(Report.Type.OK,report.getType());

        userStory = new UserStory("katılımcı olarak"
                ,"otomatlarda banka kartı kullanabilmek istiyorum"
                ,null
                ,UserStory.Type.TYPE_RR);
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
        Report report = userStoryAnalyser.analyseSentence(userStory);
        assertNotNull(report);
        assertEquals(1, report.getMessages().size());
        assertEquals("Kullanıcı hikayesinin rol kısmı yüklem içermemelidir. Yüklemler: [olsun, olsun]"
                , report.getMessages().get(0));

        userStory = new UserStory("son kullanıcı olarak"
                ,"hesap etkinliğime ilişkin bir rapor almak istiyorum"
                ,"her şeyin yolunda olduğunu kontrol edeceğim için"
                ,UserStory.Type.TYPE_RBR);
        report = userStoryAnalyser.analyseSentence(userStory);
        assertNotNull(report);
        assertEquals(1, report.getMessages().size());
        assertEquals("Kullanıcı hikayesinin fayda kısmı yüklem içermemelidir. Yüklemler: [edeceğim]"
                , report.getMessages().get(0));
    }

}