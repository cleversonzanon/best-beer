package beer.me.bestbeer.service;

import beer.me.bestbeer.domain.Playlist;

public interface SpotifyService {

  Playlist findPlaylistByBeerStyle(final String beerStyle);
}
