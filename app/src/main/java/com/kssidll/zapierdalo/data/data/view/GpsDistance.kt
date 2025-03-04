package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView

// an ungodly distance between points calculation based on sqrt estimation
// coz there's no sqrt function available
@DatabaseView(
    """
        SELECT 
            g1.runActionId, 
            SUM(
                0.5 * (
                  (0.5 * (
                    (0.5 * (
                      (0.5 * (
                        (0.5 * (
                          1 + (
                            ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                            + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                            + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                          )
                        )) + (
                          (
                            ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                            + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                            + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                          ) / (0.5 * (
                            1 + (
                              ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                              + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                              + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                          )
                          ))
                        )
                      )) + (
                        (
                          ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                          + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                          + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                        ) / (0.5 * (
                          (0.5 * (
                            1 + (
                              ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                              + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                              + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                            )
                          )) + (
                            (
                              ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                              + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                              + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                            ) / (0.5 * (
                              1 + (
                                ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                              )
                            ))
                          )
                        ))
                      )
                    )) + (
                      (
                        ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                        + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                        + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                      ) / (0.5 * (
                        (0.5 * (
                          (0.5 * (
                            1 + (
                              ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                              + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                              + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                            )
                          )) + (
                            (
                              ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                              + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                              + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                            ) / (0.5 * (
                              1 + (
                                ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                              )
                            ))
                          )
                        )) + (
                          (
                            ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                            + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                            + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                          ) / (0.5 * (
                            (0.5 * (
                              1 + (
                                ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                              )
                            )) + (
                              (
                                ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                              ) / (0.5 * (
                                1 + (
                                  ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                  + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                  + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                                )
                              ))
                            )
                          ))
                        )
                      ))
                    )
                  )) + (
                    (
                      ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                      + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                      + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                    ) / (0.5 * (
                      (0.5 * (
                        (0.5 * (
                          (0.5 * (
                            1 + (
                              ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                              + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                              + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                            )
                          )) + (
                            (
                              ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                              + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                              + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                            ) / (0.5 * (
                              1 + (
                                ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                              )
                            ))
                          )
                        )) + (
                          (
                            ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                            + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                            + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                          ) / (0.5 * (
                            (0.5 * (
                              1 + (
                                ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                              )
                            )) + (
                              (
                                ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                            ) / (0.5 * (
                                1 + (
                                  ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                  + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                  + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                                )
                              ))
                            )
                          ))
                        )
                      )) + (
                        (
                          ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                          + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                          + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                        ) / (0.5 * (
                          (0.5 * (
                            (0.5 * (
                              1 + (
                                ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                              )
                            )) + (
                              (
                                ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                              ) / (0.5 * (
                                1 + (
                                  ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                  + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                  + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                                )
                              ))
                            )
                          )) + (
                            (
                              ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                              + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                              + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                            ) / (0.5 * (
                              (0.5 * (
                                1 + (
                                  ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                  + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                  + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                                )
                              )) + (
                                (
                                  ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                  + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                  + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                                ) / (0.5 * (
                                  1 + (
                                    ((g1.longitude - g2.longitude) * (g1.longitude - g2.longitude))
                                    + ((g1.latitude - g2.latitude) * (g1.latitude - g2.latitude))
                                    + ((g1.altitude - g2.altitude) * (g1.altitude - g2.altitude))
                                  )
                                ))
                              )
                            ))
                          )
                        ))
                      )
                    ))
                  )
                )
            ) AS totalDistance
        FROM 
            GpsEntity g1
        JOIN 
            GpsEntity g2 ON g1.runActionId = g2.runActionId 
                AND g1.id = (
                    SELECT MAX(g3.id) 
                    FROM GpsEntity g3 
                    WHERE g3.id < g2.id AND g3.runActionId = g2.runActionId
                )
        GROUP BY 
            g1.runActionId
    """,
    viewName = "GpsDistance"
)
data class GpsDistance(
    val runActionId: Long,
    val totalDistance: Double
)
