package com.example.fiction_place1.domain.webtoon_episode.repository;

import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebToonEpisodeRepository extends JpaRepository<WebToonEpisode,Long> {
    List<WebToonEpisode> findByWebToonId(Long webtoonId);
}
