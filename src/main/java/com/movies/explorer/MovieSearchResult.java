package com.movies.explorer;

import java.util.Set;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = com.movies.explorer.ImmutableMovieSearchResult.class)
@JsonDeserialize(as = com.movies.explorer.ImmutableMovieSearchResult.class)
public interface MovieSearchResult {
    Set<MovieData> movies();

    class Builder extends com.movies.explorer.ImmutableMovieSearchResult.Builder {}

    static Builder builder() {
        return new Builder();
    }

}
