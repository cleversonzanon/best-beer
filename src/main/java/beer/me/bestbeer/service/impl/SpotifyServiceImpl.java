package beer.me.bestbeer.service.impl;

import beer.me.bestbeer.domain.Playlist;
import beer.me.bestbeer.domain.Track;
import beer.me.bestbeer.exception.FetchSpotifyPlaylistException;
import beer.me.bestbeer.exception.PlaylistNotFoundException;
import beer.me.bestbeer.exception.SpotifyCredentialsException;
import beer.me.bestbeer.exception.TrackWithoutArtistException;
import beer.me.bestbeer.service.SpotifyService;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** The implementation of SpotifyService */
@Log4j2
@Service
public class SpotifyServiceImpl implements SpotifyService {

  private static final String CLIENT_ID = "26b5ff21c01f4390baf7b8c23b4cc4ba";
  private static final String CLIENT_SECRET = "9ddea8189dd04fcda9e81347119a04ca";
  private static final String SPOTIFY_URL_KEY = "spotify";

  private final SpotifyApi spotifyApi;
  private final ClientCredentialsRequest clientCredentialsRequest;

  /** Instantiates a new Spotify service. */
  public SpotifyServiceImpl() {
    // Build the api object request
    this.spotifyApi =
        SpotifyApi.builder().setClientId(CLIENT_ID).setClientSecret(CLIENT_SECRET).build();
    // Build the client request, in order to retrieve the credentials
    this.clientCredentialsRequest = spotifyApi.clientCredentials().build();
  }

  /**
   * Find a playlist by beer style
   *
   * @param beerStyle the beer style
   * @return the found playlist
   */
  @Override
  public Playlist findPlaylistByBeerStyle(final String beerStyle) {
    // If null, throw exception
    if (StringUtils.isEmpty(beerStyle)) {
      throw new PlaylistNotFoundException();
    }
    // Retrieve and set the credentials into the api object
    this.retrieveCredentials();
    // Find a playlist by beer style
    PlaylistSimplified playlistSimplified = this.findSpotifyPlaylistByBeerStyle(beerStyle);
    // Find the tracks of the playlist
    List<Track> tracks = findTracksByPlaylist(playlistSimplified);
    // Return the built playlist object
    return Playlist.builder().name(playlistSimplified.getName()).tracks(tracks).build();
  }

  /**
   * Find a spotify playlist by beer style
   *
   * @param beerStyle the beer style
   * @return the found playlist
   */
  private PlaylistSimplified findSpotifyPlaylistByBeerStyle(final String beerStyle) {
    try {
      // Build the playlist search request object
      final SearchPlaylistsRequest searchPlaylistsRequest =
          this.spotifyApi.searchPlaylists(beerStyle).limit(1).offset(1).build();
      // Execute the request
      final Paging<PlaylistSimplified> playlistSimplifiedPaging = searchPlaylistsRequest.execute();
      // Get and return the first playlist found, or else throw an exception
      return Arrays.stream(playlistSimplifiedPaging.getItems())
          .findFirst()
          .orElseThrow(() -> new PlaylistNotFoundException(beerStyle));

    } catch (IOException | SpotifyWebApiException e) {
      throw new FetchSpotifyPlaylistException(e);
    }
  }

  /**
   * Find playlist's tracks
   *
   * @param playlistSimplified the playlist object
   * @return the list of found tracks
   */
  private List<Track> findTracksByPlaylist(final PlaylistSimplified playlistSimplified) {
    try {
      // Build the playlist's track request
      final GetPlaylistsTracksRequest getPlaylistsTracksRequest =
          spotifyApi.getPlaylistsTracks(playlistSimplified.getId()).limit(10).build();
      // Execute the request
      final Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();
      // For each track found, build and return a track dto
      return Arrays.stream(playlistTrackPaging.getItems())
          .map(PlaylistTrack::getTrack)
          .map(
              spotifyTrack -> {
                // Get any track's artist, or else throw an exception
                ArtistSimplified artist =
                    Arrays.stream(spotifyTrack.getArtists())
                        .findFirst()
                        .orElseThrow(() -> new TrackWithoutArtistException(spotifyTrack.getName()));
                // Build the return object
                return Track.builder()
                    .name(spotifyTrack.getName())
                    .artist(artist.getName())
                    .link(
                        artist
                            .getExternalUrls()
                            .get(SPOTIFY_URL_KEY)) // Get the url by key in a map
                    .build();
              })
          .collect(Collectors.toList());

    } catch (IOException | SpotifyWebApiException e) {
      throw new FetchSpotifyPlaylistException(e);
    }
  }

  /** Retrieve and set the spotify api credentials */
  private void retrieveCredentials() {
    try {
      // Execute the client credentials request
      final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
      // Set into the API object
      spotifyApi.setAccessToken(clientCredentials.getAccessToken());

    } catch (IOException | SpotifyWebApiException e) {
      throw new SpotifyCredentialsException(e);
    }
  }
}
