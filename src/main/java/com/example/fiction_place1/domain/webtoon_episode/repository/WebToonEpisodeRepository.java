package com.example.fiction_place1.domain.webtoon_episode.repository;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon_episode.entity.WebToonEpisode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface WebToonEpisodeRepository extends JpaRepository<WebToonEpisode,Long> {
    List<WebToonEpisode> findByWebToonId(Long webtoonId);
    Page<WebToonEpisode> findByWebToonIdOrderByCreatedDateDesc(Long webtoonId, Pageable pageable);
}
