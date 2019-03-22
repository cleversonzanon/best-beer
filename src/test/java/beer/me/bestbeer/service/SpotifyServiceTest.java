package beer.me.bestbeer.service;

import beer.me.bestbeer.domain.Playlist;
import beer.me.bestbeer.exception.PlaylistNotFoundException;
import beer.me.bestbeer.service.impl.SpotifyServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
public class SpotifyServiceTest {

  // Service
  private SpotifyService spotifyService;

  @Before
  public void setup() {
    // Service
    this.spotifyService = new SpotifyServiceImpl();
  }

  @Test
  public void givenBeerStyle_whenFindPlaylistByBeerStyle_thenMustReturnPlaylist() {
    // GIVEN
    String beerStyle = "Imperial Stout";
    // WHEN
    Playlist playlist = this.spotifyService.findPlaylistByBeerStyle(beerStyle);
    // THEN
    assertNotNull(playlist);
    assertNotNull(playlist.getName());
    assertNotNull(playlist.getTracks());
    assertTrue(playlist.getTracks().size() > 0);
  }

  @Test(expected = PlaylistNotFoundException.class)
  public void givenNull_whenFindPlaylistByBeerStyle_thenMustThrowException() {
    // Method call
    this.spotifyService.findPlaylistByBeerStyle(null);
  }
}
