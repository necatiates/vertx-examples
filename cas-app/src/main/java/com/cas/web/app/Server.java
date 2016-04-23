package com.cas.web.app;

import com.cas.spring.entity.Cash;
import com.cas.web.app.handlers.*;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.cas.spring.context.ExampleSpringConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Server{
  private static String WEBROOT_FOLDER =  "cas-app/webroot";
  private static String PRIVATE_FOLDER =  "cas-app/private";


  /*private static String WEBROOT_FOLDER =  "webroot";
  private static String PRIVATE_FOLDER =  "private";*/
  public static EntityManagerFactory factory;

  public static void main(String[] args) throws Exception {
    System.out.println(System.getProperty("user.dir"));
    start();
  }

  public static void start() throws Exception {

    final Vertx vertx = Vertx.vertx();

    ApplicationContext context = new AnnotationConfigApplicationContext(ExampleSpringConfiguration.class);

    factory =   (EntityManagerFactory)context.getBean("helloBean");
    Router router = initRoutes();
    initCashes();
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }

  private static void initCashes() {
    EntityManager em =factory.createEntityManager();
    Cash slotCash = em.find(Cash.class,"Slots");
    em.getTransaction().begin();
    if(slotCash == null){
      slotCash = new Cash();
      slotCash.setCash(0.0);
      slotCash.setGame("Slots");
      slotCash.setCapital(0.0);
      em.persist(slotCash);
    }
    Cash strachCash = em.find(Cash.class,"Strach");
    if(strachCash == null){
      strachCash = new Cash();
      strachCash.setCash(0.0);
      strachCash.setGame("Strach");
      strachCash.setCapital(0.0);
      em.persist(strachCash);
    }
    Cash cardsCash = em.find(Cash.class,"Cards");
    if(cardsCash == null){
      cardsCash = new Cash();
      cardsCash.setCash(0.0);
      cardsCash.setGame("Cards");
      cardsCash.setCapital(0.0);
      em.persist(cardsCash);
    }
    em.getTransaction().commit();
  }

  private static Router initRoutes() {
    Router router = Router.router(Vertx.vertx());

    // We need cookies, sessions and request bodies
    router.route().handler(CookieHandler.create());
    router.route().handler(BodyHandler.create());
    router.route().handler(SessionHandler.create(LocalSessionStore.create(Vertx.vertx())));

    // Simple auth service which uses a JDBC data source

    // Serve the static private pages from directory 'private'
    router.route("/private/*").handler(AuthorizedPathHandler.create().setCachingEnabled(false).setWebRoot(PRIVATE_FOLDER).setRole("user"));

    // Handles the actual login
    router.route("/loginhandler").handler(CasFormLoginHandler.create().setDirectLoggedInOKURL("/private/games.html"));

    //Handler registration
    router.route("/registerhandler").handler(FormRegisterHandler.create().setReturnURLParam("/index.html"));

    router.post("/bet/slotmachine").handler(SlotMachineBetHandler::bet);
    router.post("/bet/accept").handler(SlotMachineBetAcceptHandler::accept);
    router.get("/bet/info").handler(InfoServiceHandler::getInfo);
    router.post("/poker/bet").handler(PokerBetHandler::bet);
    router.post("/poker/accept").handler(PokerBetAcceptHandler::accept);
    router.post("/bet/strach").handler(StrachBetHandler::bet);
    router.post("/scratch/accept").handler(StrachBetAcceptHandler::accept);
    router.post("/bet/blackjack").handler(BlackJackBetHandler::bet);
    router.post("/blackjack/accept").handler(BlackJackBetAcceptHandler::accept);
    router.get("/tokengen").handler(BrainTreeTokanizerHandler::getToken);
    router.post("/checkout").handler(BrainTreeCheckoutHandler::checkout);
    router.post("/creditCardHandler").handler(CreditCardHandler::handleCardOK);

    router.get("/admin/gameCash").handler(GameCashHandler::getCashes);
    router.post("/admin/gameCash").handler(GameCashHandler::updateCash);
    router.post("/admin/users").handler(UserAdminHandler::getUsers);
    router.post("/admin/users").handler(UserAdminHandler::updateuser);

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
    // Implement logout
    router.route("/logout").handler(context -> {
      context.clearUser();
      // Redirect back to the index page
      context.response().putHeader("location", "/").setStatusCode(302).end();
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

