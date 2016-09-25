/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Tiggi.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;
import com.example.RandomTasks;

@Api(
    name = "myApi",
    version = "v1",
    description = "Random tasks for Reward Bingo"
)
public class MyEndpoint {

    @ApiMethod(name = "fetchTasks")
    public MyBean fetchTasks(@Named("num") int n) {
        RandomTasks taskClass = new RandomTasks();
        MyBean response = new MyBean();
        response.setData(taskClass.getRandomTasks(n));

        return response;
    }
}
