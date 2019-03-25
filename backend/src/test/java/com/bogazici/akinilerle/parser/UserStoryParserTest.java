package com.bogazici.akinilerle.parser;

import com.bogazici.akinilerle.model.UserStory;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserStoryParserTest {

    UserStoryParser userStoryParser = new UserStoryParser();

    @Test
    public void it_should_parse_format_1() throws Exception {
        UserStory userStory1 = userStoryParser.parseSingle("Sisteme kayıtlı bir üye olarak, kredi kartı ya" +
                " da debit kart ile 2 ödeme seçeneğimin olmasını istiyorum, böylece etkinlik bileti için sadece elden" +
                " nakit ile ödeme yapmak zorunda kalmayacağım.\n");

        assertEquals(userStory1.getRole(),"sisteme kayıtlı bir üye olarak");
        assertEquals(userStory1.getRequest(),"kredi kartı ya da debit kart ile 2 ödeme seçeneğimin olmasını istiyorum");
        assertEquals(userStory1.getBenefit(),"böylece etkinlik bileti için sadece elden nakit ile ödeme yapmak zorunda kalmayacağım");
        assertEquals(userStory1.getType(),UserStory.Type.TYPE_1);

        UserStory userStory2 = userStoryParser.parseSingle("Etkinlik düzenlemek isteyen bir kuruluş olarak, " +
                "farklı bir arayüz kullanmak istiyoruz, böylece gerçekleşecek etkinlikleri görmekten çok etkinliğimiz " +
                "için kiralayabileceğimiz uygun yerleri görmüş olacağız.");

        assertEquals(userStory2.getRole(),"etkinlik düzenlemek isteyen bir kuruluş olarak");
        assertEquals(userStory2.getRequest(),"farklı bir arayüz kullanmak istiyoruz");
        assertEquals(userStory2.getBenefit(),"böylece gerçekleşecek etkinlikleri görmekten çok etkinliğimiz" +
                " için kiralayabileceğimiz uygun yerleri görmüş olacağız");
        assertEquals(userStory2.getType(),UserStory.Type.TYPE_1);

        UserStory userStory3 = userStoryParser.parseSingle("Ben KFO olarak online bilet satışını yapabilmek" +
                " için belli bir banka ile anlaşmak istiyorum böylece katılımcıların güvenli bir şekilde bilet " +
                "aldıklarını onlara hissettirebileceğim.");

        assertEquals(userStory3.getRole(),"ben kfo olarak");
        assertEquals(userStory3.getRequest(),"online bilet satışını yapabilmek için belli bir banka ile anlaşmak istiyorum");
        assertEquals(userStory3.getBenefit(),"böylece katılımcıların güvenli bir şekilde bilet aldıklarını onlara hissettirebileceğim");
        assertEquals(userStory3.getType(),UserStory.Type.TYPE_1);
    }

    @Test
    public void it_should_parse_format_2() throws Exception {
        UserStory userStory1 = userStoryParser.parseSingle("Rapor uzmanı olarak, verileri kolaylıkla " +
                "bulabilmek için, listeler içerisinde arama fonksiyonuna ihtiyacım var.");

        assertEquals(userStory1.getRole(),"rapor uzmanı olarak");
        assertEquals(userStory1.getBenefit(),"verileri kolaylıkla bulabilmek için");
        assertEquals(userStory1.getRequest(),"listeler içerisinde arama fonksiyonuna ihtiyacım var");
        assertEquals(userStory1.getType(),UserStory.Type.TYPE_2);

        UserStory userStory2 = userStoryParser.parseSingle("Son kullanıcı olarak, her şeyin yolunda olduğunu" +
                " kontrol etmek için, hesap etkinliğime ilişkin bir rapor almak istiyorum.");

        assertEquals(userStory2.getRole(),"son kullanıcı olarak");
        assertEquals(userStory2.getBenefit(),"her şeyin yolunda olduğunu kontrol etmek için");
        assertEquals(userStory2.getRequest(),"hesap etkinliğime ilişkin bir rapor almak istiyorum");
        assertEquals(userStory2.getType(),UserStory.Type.TYPE_2);

        UserStory userStory3 = userStoryParser.parseSingle("Sistem Yöneticisi olarak, kullanıcının sisteme " +
                "girmesi ve yetkileri çerçevesinde işlem yapabilmesi için, kullanıcıyı sisteme eklemek istiyorum.");

        assertEquals(userStory3.getRole(),"sistem yöneticisi olarak");
        assertEquals(userStory3.getBenefit(),"kullanıcının sisteme girmesi ve yetkileri çerçevesinde işlem yapabilmesi için");
        assertEquals(userStory3.getRequest(),"kullanıcıyı sisteme eklemek istiyorum");
        assertEquals(userStory3.getType(),UserStory.Type.TYPE_2);
    }

    @Test
    public void it_should_parse_format_3() throws Exception {
        UserStory userStory1 = userStoryParser.parseSingle("Organizator olarak etkinlik alaninda " +
                "katılımcıların bilekliklerine para yükleyebilecekleri otomatlar istiyorum.");

        assertEquals(userStory1.getRole(),"organizator olarak");
        assertEquals(userStory1.getRequest(),"etkinlik alaninda katılımcıların bilekliklerine " +
                "para yükleyebilecekleri otomatlar istiyorum");
        assertNull(userStory1.getBenefit());
        assertEquals(userStory1.getType(),UserStory.Type.TYPE_3);

        UserStory userStory2 = userStoryParser.parseSingle("Katılımcı olarak otomatlarda banka kartı" +
                " kullanabilmek istiyorum.");

        assertEquals(userStory2.getRole(),"katılımcı olarak");
        assertEquals(userStory2.getRequest(),"otomatlarda banka kartı kullanabilmek istiyorum");
        assertNull(userStory2.getBenefit());
        assertEquals(userStory2.getType(),UserStory.Type.TYPE_3);


        UserStory userStory3 = userStoryParser.parseSingle("Katılımcı olarak etkinlik alaninda yiyecek" +
                " ve içecek satın almak istiyorum.");

        assertEquals(userStory3.getRole(),"katılımcı olarak");
        assertEquals(userStory3.getRequest(),"etkinlik alaninda yiyecek ve içecek satın almak istiyorum");
        assertNull(userStory3.getBenefit());
        assertEquals(userStory3.getType(),UserStory.Type.TYPE_3);
    }



}