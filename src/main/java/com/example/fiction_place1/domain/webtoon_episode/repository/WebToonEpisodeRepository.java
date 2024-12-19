package com.example.fiction_place1.domain.webtoon_episode.repository;

import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface WebToonEpisodeRepository extends JpaRepository<WebToonEpisode,Long> {
    List<WebToonEpisode> findByWebToonId(Long webtoonId);
    List<WebToonEpisode> findByIsSelectedTrue();
}
