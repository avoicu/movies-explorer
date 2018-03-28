package com.movies.explorer;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.util.List;
import java.util.function.Supplier;

public class MovieSearchApplication extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new MovieSearchApplication().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        Supplier<Connection> connectionSupplier = new ConnectionSupplier();
        MovieDatabase movieDatabase = new MovieDatabase(connectionSupplier);
        List<MovieData> movies = WikipediaParser.getMovies();
        movieDatabase.populateDatabase(movies);
        MovieSearchResource movieSearchResource = new MovieSearchResource(movieDatabase);
        environment.jersey().register(movieSearchResource);
    }
}
