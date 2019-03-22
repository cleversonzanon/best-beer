package beer.me.bestbeer.service;

import beer.me.bestbeer.domain.Playlist;

/** Service related to Spotify API requests */
public interface SpotifyService {

  /**
   * Find a playlist by beer style
   *
   * @param beerStyle the beer style
   * @return the found playlist
   */
  Playlist findPlaylistByBeerStyle(final String beerStyle);
}
