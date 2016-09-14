package com.cas.web.app;

import com.cas.StaticDefinitions;
import com.cas.spring.entity.Admin;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.GlobalSettings;
import com.cas.web.app.handlers.game.accept.*;
import com.cas.web.app.handlers.game.authorization.AuthorizedPathHandler;
import com.cas.web.app.handlers.game.authorization.CasAdminLoginHandler;
import com.cas.web.app.handlers.game.authorization.CasFormLoginHandler;
import com.cas.web.app.handlers.game.authorization.FormRegisterHandler;
import com.cas.web.app.handlers.game.desicion.*;
import com.cas.web.app.handlers.game.info.GameCashHandler;
import com.cas.web.app.handlers.game.info.InfoServiceHandler;
import com.cas.web.app.handlers.game.info.SettingsHandler;
import com.cas.web.app.handlers.game.info.UserAdminHandler;
import com.cas.web.app.handlers.game.payment.*;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.cas.spring.context.ExampleSpringConfiguration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;

public class Server{
  private static String WEBROOT_FOLDER =  "cas-app/webroot";
  private static String PRIVATE_FOLDER =  "cas-app/private";
  private static String ADMIN_FOLDER =    "cas-app/webroot/casadmin";


  /*private static String WEBROOT_FOLDER =  "webroot";
  private static String PRIVATE_FOLDER =  "private";*/
  public static SessionFactory factory;

  public static void main(String[] args) throws Exception {
    System.out.println(System.getProperty("user.dir"));
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    Ignite ignite = Ignition.start(classloader.getResource("ignite-conf.xml"));
    ignite.getOrCreateCache("org.hibernate.cache.spi.UpdateTimestampsCache");
    ignite.getOrCreateCache("org.hibernate.cache.internal.StandardQueryCache");

    start();
  }

  public static void start() throws Exception {

    final Vertx vertx = Vertx.vertx();

    //ApplicationContext context = new AnnotationConfigApplicationContext(ExampleSpringConfiguration.class);
    ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");

    factory =   (SessionFactory)context.getBean("sessionFactory");
    Router router = initRoutes();
    initCashes();
    initPreferences();
    initAdmins();

    HttpServerOptions httpOpts = new HttpServerOptions();
    httpOpts.setPemKeyCertOptions(new PemKeyCertOptions()
            .setCertPath("cas-app/shambala.bet.pem")
            .setKeyPath("cas-app/shambala.bet.key"));
    httpOpts.setSsl(true);
    vertx.createHttpServer(httpOpts).requestHandler(router::accept).listen(443);
  }

  private static void initAdmins() {

    Session em = factory.openSession();
    em.getTransaction().begin();

    Admin admin = (Admin) em.get(Admin.class,"casadmin");
    if(admin == null){
      admin = new Admin();
      admin.setUsername("casadmin");
      admin.setPassword("uzunincebiryoldayim24212421");
      em.persist(admin);
    }
    em.getTransaction().commit();
    em.close();
  }

  private static void initPreferences() {
    Session em = factory.openSession();
    em.getTransaction().begin();
    GlobalSettings winPerc = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.WIN_PERCENTAGE_SETTINGS_NAME);
    if(winPerc == null){
      winPerc = new GlobalSettings();
      winPerc.setName(StaticDefinitions.WIN_PERCENTAGE_SETTINGS_NAME);
      winPerc.setValue("49");
      em.persist(winPerc);
    }
    GlobalSettings freeSpinPerc = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.FREE_SPIN_SETTINGS_NAME);
    if(freeSpinPerc == null){
      freeSpinPerc = new GlobalSettings();
      freeSpinPerc.setName(StaticDefinitions.FREE_SPIN_SETTINGS_NAME);
      freeSpinPerc.setValue("30");
      em.persist(freeSpinPerc);
    }
    GlobalSettings bonusPerc = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.BONUS_SETTINGS_NAME);
    if(bonusPerc == null){
      bonusPerc = new GlobalSettings();
      bonusPerc.setName(StaticDefinitions.BONUS_SETTINGS_NAME);
      bonusPerc.setValue("20");
      em.persist(bonusPerc);
    }
    GlobalSettings bonusGiveAwayPerc = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.BONUS_GIVEAWAY_PERCENTAGE_SETTINGS);
    if(bonusGiveAwayPerc == null){
      bonusGiveAwayPerc = new GlobalSettings();
      bonusGiveAwayPerc.setName(StaticDefinitions.BONUS_GIVEAWAY_PERCENTAGE_SETTINGS);
      bonusGiveAwayPerc.setValue("20");
      em.persist(bonusGiveAwayPerc);
    }
    GlobalSettings absoulteStopValue = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.ABSOLUTE_STOP_VALUE);
    if (absoulteStopValue == null) {
      absoulteStopValue = new GlobalSettings();
      absoulteStopValue.setName(StaticDefinitions.ABSOLUTE_STOP_VALUE);
      absoulteStopValue.setValue("0");
      em.persist(absoulteStopValue);
    }
    GlobalSettings cashGiveAway = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.CASH_GIVEAWAY_PERCENTAGE);
    if(cashGiveAway == null){
      cashGiveAway = new GlobalSettings();
      cashGiveAway.setName(StaticDefinitions.CASH_GIVEAWAY_PERCENTAGE);
      cashGiveAway.setValue("50");
      em.persist(cashGiveAway);
    }
    em.getTransaction().commit();
    em.close();
  }

  private static void initCashes() {
    Session em = factory.openSession();
    Cash slotCash = (Cash) em.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
    em.getTransaction().begin();
    if(slotCash == null){
      slotCash = new Cash();
      slotCash.setCash(0.0);
      slotCash.setGame(StaticDefinitions.GAME_CASH_NAME);
      em.persist(slotCash);
    }
    Cash strachCash = (Cash) em.get(Cash.class,StaticDefinitions.BONUS_CASH_NAME);
    if(strachCash == null){
      strachCash = new Cash();
      strachCash.setCash(0.0);
      strachCash.setGame(StaticDefinitions.BONUS_CASH_NAME);
      em.persist(strachCash);
    }
    em.getTransaction().commit();
    em.close();
  }

  private static Router initRoutes() {
    Router router = Router.router(Vertx.vertx());

    // We need cookies, sessions and request bodies
    router.route().handler(CookieHandler.create());
    router.route().handler(BodyHandler.create());
    router.route().handler(SessionHandler.create(LocalSessionStore.create(Vertx.vertx())));

    // Simple auth service which uses a JDBC data source

    // Serve the static private pages from directory 'private'
    router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot(PRIVATE_FOLDER));
    router.route("/casadmin/*").handler(AuthorizedPathHandler.create().setCachingEnabled(false).setWebRoot(ADMIN_FOLDER).setRole(StaticDefinitions.ADMIN_SESSION_KEY));

    // Handles the actual login
    router.route("/loginhandler").handler(CasFormLoginHandler.create().setDirectLoggedInOKURL("/private/games.html"));
    router.route("/adminLogin").handler(CasAdminLoginHandler.create().setDirectLoggedInOKURL("/casadmin/index.html"));

    //Handler registration
    router.route("/registerhandler").handler(FormRegisterHandler.create().setReturnURLParam("/index.html"));

    SlotMachineBetHandler slotMachineBetHandler = new SlotMachineBetHandler();
    router.post("/bet/slotmachine").handler(slotMachineBetHandler);
    router.post("/bet/accept").handler(SlotMachineBetAcceptHandler::accept);
    router.post("/bonusSlot/accept").handler(BonusAcceptHandler::accept);

    router.get("/bet/info").handler(InfoServiceHandler::getInfo);
    router.post("/poker/bet").handler(PokerBetHandler::bet);
    router.post("/poker/accept").handler(PokerBetAcceptHandler::accept);
    router.post("/bet/strach").handler(StrachBetHandler::bet);
    router.post("/scratch/accept").handler(StrachBetAcceptHandler::accept);
    router.post("/bet/blackjack").handler(BlackJackBetHandler::bet);
    router.post("/blackjack/accept").handler(BlackJackBetAcceptHandler::accept);

    router.post("/bingo/bet").handler(BingoBetHandler::bet);
    router.post("/bingo/accept").handler(BingoBetAcceptHandler::accept);

    router.post("/running/bet").handler(RunningBetHandler::bet);
    router.post("/running/accept").handler(RunningAcceptHandler::accept);
    router.post("/admin/runningGames").handler(RunningBetHandler::getGames);

    router.post("/miner/bet").handler(MinerBetHandler::bet);
    router.post("/miner/accept").handler(MinerAcceptHandler::accept);
    router.post("/admin/goldMinerGames").handler(MinerBetHandler::getGames);

    router.get("/tokengen").handler(BrainTreeTokanizerHandler::getToken);
    router.post("/checkout").handler(BrainTreeCheckoutHandler::checkout);
    router.post("/creditCardHandler").handler(CreditCardHandler::handleCardOK);

    router.get("/admin/gameCash").handler(GameCashHandler::getCashes);
    router.post("/admin/gameCash").handler(GameCashHandler::updateCash);
    router.post("/admin/users").handler(UserAdminHandler::getUsers);
    router.post("/admin/users").handler(UserAdminHandler::updateuser);
    router.post("/admin/setPassToUser").handler(UserAdminHandler::setPasswordForUser);

    router.post("/transfer/checkout").handler(TransferCheckoutHandler::handle);
    router.post("/transfer/checkin").handler(TransferCheckinHandler::handle);
    router.post("/transfer/checkout_history").handler(TransferCheckoutHandler::getTransfers);
    router.post("/transfer/checkout_flag").handler(TransferCheckoutHandler::changeFlag);
    router.post("/transfer/checkin_history").handler(TransferCheckinHandler::getTransfers);
    router.post("/transfer/checkin_flag").handler(TransferCheckinHandler::changeFlag);

    router.post("/admin/slotGames").handler(SlotMachineBetHandler::getGames);
    router.post("/admin/strachGames").handler(StrachBetHandler::getGames);
    router.post("/admin/blackJackGames").handler(BlackJackBetHandler::getGames);
    router.post("/admin/pokerGames").handler(PokerBetHandler::getGames);

    router.get("/admin/settings").handler(SettingsHandler::getSettings);
    router.post("/admin/settings").handler(SettingsHandler::updateSettings);

    // Implement logout
    router.route("/logout").handler(context -> {
      context.clearUser();
      context.session().destroy();
      // Redirect back to the index page
      context.response().putHeader("location", "/").setStatusCode(302).end();
    });

    router.route("/admin/logout").handler(context -> {
      context.clearUser();
      context.session().destroy();
      // Redirect back to the index page
      context.response().putHeader("location", "/casadmin/login.html").setStatusCode(302).end();
    });

    // Serve the non private static pages
    StaticHandler handler = StaticHandler.create().setCachingEnabled(false).setWebRoot(WEBROOT_FOLDER);
    router.route().handler(context -> {
      if(context.user() == null) {
        handler.handle(context);
      }else{
        context.response().putHeader("location", "/private/games.html").setStatusCode(302).end();
      }
    });


    return router;
  }
}

