package com.itmo.nxzage.server.services.net;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.common.util.net.PacketWrapper;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.net.InteractionContext;

public class CacheService {
    private static final int MAX_CACHE_SIZE = 1000;
    private static final int MIN_CACHE_SIZE = 200;
    private final Map<UUID, List<PacketWrapper>> cache;
    private final Logger logger = ServerLogger.getLogger("Cache");
    
    {
        cache = new LinkedHashMap<UUID, List<PacketWrapper>>();
    }

    public boolean hit(InteractionContext interaction) {
        if (interaction.getID() == null) {
            logger.warning("Attempt to hit unidentifiable interaction");
            throw new IllegalArgumentException("Unidentifiable interaction");
        }
        UUID interactionID = interaction.getID();
        if (!cache.containsKey(interactionID)) {
            logger.info("Interaction wasn\'t hit in cache");
            return false;
        }
        List<PacketWrapper> responses = cache.get(interactionID);
        interaction.setResponses(responses);
        logger.info("Interaction hitted and Interaction Context successfully filled with response packets");
        return true;
    }

    public void memorize(InteractionContext interaction) {
        if (!isCachable(interaction)) {
            logger.info("Uncachable interactioin skipped");
            return;
        }
        UUID interactionID = interaction.getID();
        List<PacketWrapper> responses = interaction.getResponses();
        cache.put(interactionID, responses);
        logger.info("Interaction memorized");
        clear();
    }

    private void clear() {
        if (cache.size() < MAX_CACHE_SIZE) {
            return;
        }

        var iterator = cache.entrySet().iterator();
        int erased = 0;
        while (cache.size() > MIN_CACHE_SIZE && iterator.hasNext()) {
            iterator.next();
            iterator.remove();
            erased++;
        }
        logger.info("Cache cleaned. Erased " + Integer.toString(erased) + " records");
    }

    private boolean isCachable(InteractionContext interaction) {
        return (interaction.getResponses().size() == 1 && interaction.getResponses().getFirst().getType().equals(PacketType.RESPONSE));
    }
}
