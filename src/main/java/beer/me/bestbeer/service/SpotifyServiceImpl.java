package beer.me.bestbeer.service;

import beer.me.bestbeer.domain.Playlist;
import beer.me.bestbeer.domain.Track;
import beer.me.bestbeer.exception.FetchSpotifyPlaylistException;
import beer.me.bestbeer.exception.PlaylistNotFoundException;
import beer.me.bestbeer.exception.SpotifyCredentialsException;
import beer.me.bestbeer.exception.TrackWithoutArtistException;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SpotifyServiceImpl implements SpotifyService {

  private static final String CLIENT_ID = "26b5ff21c01f4390baf7b8c23b4cc4ba";
  private static final String CLIENT_SECRET = "9ddea8189dd04fcda9e81347119a04ca";
  private static final String SPOTIFY_URL_KEY = "spotify";

  private final SpotifyApi spotifyApi;
  private final ClientCredentialsRequest clientCredentialsRequest;

  public SpotifyServiceImpl() {
    this.spotifyApi =
        SpotifyApi.builder().setClientId(CLIENT_ID).setClientSecret(CLIENT_SECRET).build();
    this.clientCredentialsRequest = spotifyApi.clientCredentials().build();
  }

  @Override
  public Playlist findPlaylistByBeerStyle(final String beerStyle) {
    this.retrieveCredentials();

    PlaylistSimplified playlistSimplified = this.findPlaylist(beerStyle);

    List<Track> tracks = findTracksByPlaylist(playlistSimplified);

    return Playlist.builder().name(playlistSimplified.getName()).tracks(tracks).build();
  }

  private List<Track> findTracksByPlaylist(final PlaylistSimplified playlistSimplified) {
    try {
      final GetPlaylistsTracksRequest getPlaylistsTracksRequest =
          spotifyApi.getPlaylistsTracks(playlistSimplified.getId()).limit(10).build();

      final Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();

      return Arrays.stream(playlistTrackPaging.getItems())
          .map(PlaylistTrack::getTrack)
          .map(
              spotifyTrack -> {
                ArtistSimplified artist =
                    Arrays.stream(spotifyTrack.getArtists())
                        .findFirst()
                        .orElseThrow(() -> new TrackWithoutArtistException(spotifyTrack.getName()));

                return Track.builder()
                    .name(spotifyTrack.getName())
                    .artist(artist.getName())
                    .link(artist.getExternalUrls().get(SPOTIFY_URL_KEY))
                    .build();
              })
          .collect(Collectors.toList());

    } catch (IOException | SpotifyWebApiException e) {
      throw new FetchSpotifyPlaylistException(e);
    }
  }

  private PlaylistSimplified findPlaylist(final String beerStyle) {
    try {
      final SearchPlaylistsRequest searchPlaylistsRequest =
          this.spotifyApi.searchPlaylists(beerStyle).limit(1).offset(1).build();

      final Paging<PlaylistSimplified> playlistSimplifiedPaging = searchPlaylistsRequest.execute();

      return Arrays.stream(playlistSimplifiedPaging.getItems())
          .findFirst()
          .orElseThrow(() -> new PlaylistNotFoundException(beerStyle));

    } catch (IOException | SpotifyWebApiException e) {
      throw new FetchSpotifyPlaylistException(e);
    }
  }

  private void retrieveCredentials() {
    try {
      final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
      spotifyApi.setAccessToken(clientCredentials.getAccessToken());

    } catch (IOException | SpotifyWebApiException e) {
      throw new SpotifyCredentialsException(e);
    }
  }
}
