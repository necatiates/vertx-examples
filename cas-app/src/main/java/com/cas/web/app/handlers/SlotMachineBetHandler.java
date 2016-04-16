package com.cas.web.app.handlers;

import com.cas.cache.CacheManager;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.SlotBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Random;

/**
 * Created by tolga on 06.03.2016.
 */
public class SlotMachineBetHandler {
    private static Random random = new Random();
    public static void bet(RoutingContext routingContext){
        final SlotBet slotBet = Json.decodeValue(routingContext.getBodyAsString(),SlotBet.class);

        JsonObject response = new JsonObject();

        EntityManager entityManager = Server.factory.createEntityManager();
        entityManager.getTransaction().begin();
        Cash cash = entityManager.find(Cash.class,"Slots");
        User user = entityManager.find(User.class,((User)routingContext.session().get("user")).getUsername());
        slotBet.setUsername(user.getUsername());


        double ratio = CacheManager.getHappinessRatio();
        /*
            Decide to win or not
        */
        Double avaliableCash = ((cash.getCapital() * 0.20 + cash.getCash())  / 2 ) / 5;
        if(slotBet.getMinWin() < avaliableCash && ratio < 1){
            slotBet.setWinResult(true);
            response.put("_win_",true);
            response.put("_maxWin_",avaliableCash);
        }else if(slotBet.getMinWin() < avaliableCash && ratio == 1){
            boolean genRandom = random.nextBoolean();
            slotBet.setWinResult(genRandom);
            response.put("_win_",genRandom);
            response.put("_maxWin_",avaliableCash);
        } else{
            slotBet.setWinResult(false);
            response.put("_win_",false);
            response.put("_maxWin_",0);
        }
        entityManager.persist(slotBet);

        cash.setCash(cash.getCash() + slotBet.getBet() * slotBet.getLineCount());
        user.setCash(user.getCash() - slotBet.getBet() * slotBet.getLineCount());
        entityManager.getTransaction().commit();
        response.put("id",slotBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }

    public static void getGames(RoutingContext routingContext) {
        EntityManager entityManager = Server.factory.createEntityManager();
        CriteriaQuery<SlotBet> criteria = entityManager.getCriteriaBuilder().createQuery(SlotBet.class);
        criteria.select(criteria.from(SlotBet.class));
        List<SlotBet> slotBets = entityManager.createQuery(criteria).getResultList();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(slotBets));
        entityManager.close();
    }
}
