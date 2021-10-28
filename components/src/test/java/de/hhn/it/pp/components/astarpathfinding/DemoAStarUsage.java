package de.hhn.it.pp.components.astarpathfinding;

import de.hhn.it.pp.components.astarpathfinding.provider.MapManager;
import de.hhn.it.pp.components.astarpathfinding.provider.Pathfinder;
import java.util.List;

public class DemoAStarUsage {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(DemoAStarUsage.class);

  public static void main(String[] args) throws Exception {
    // Create the service
    logger.info(">>> create service");
    Pathfinder service = new Pathfinder();
    logger.info("" + service);

    // Create a 20x20 map
    logger.info(">>> create a new 20x20 map");
    service.createMap(MapManager.MAX_WIDTH, MapManager.MAX_HEIGHT);
    logger.info("" + service);

    Thread.sleep(500);

    // Set start point
    logger.info(">>> set start point");
    Position start = new Position(0, 3);
    service.setStartPoint(start);
    logger.info("Startpoint set to: " + start);

    Thread.sleep(500);

    // Set end point
    logger.info(">>> set end point");
    Position end = new Position(5, 3);
    service.setEndPoint(end);
    logger.info("Endpoint set to: " + end);

    Thread.sleep(500);

    // Add Obstacles
    for (int y = 2; y <= 4; y++) {
      logger.info(">>> place lava terrain at: " + 3 + "|" + y);
      service.placeTerrain(TerrainType.LAVA, new Position(y, 3));
      logger.info(String.format("Terrain placed at %d | %d:", 3, y) + TerrainType.LAVA);
      Thread.sleep(500);
    }

    // Start pathfinding algorithm
    logger.info(">>> pathfinding started");
    List<PathfindingInformation> results = service.doPathfinding();
    Thread.sleep(1000);
    logger.info("Shortest path found: " +  results.get(results.size() - 1).getFinalPathPositions().toString());
    logger.info("Total algorithm states: " + results.size());

    Thread.sleep(1000);

    // Reset
    logger.info(">>> reset map");
    service.reset();
    logger.info("" + service);
  }
}
