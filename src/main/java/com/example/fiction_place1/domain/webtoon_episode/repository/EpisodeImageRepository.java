package com.example.fiction_place1.domain.webtoon_episode.repository;

import com.example.fiction_place1.domain.webtoon_episode.entity.EpisodeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeImageRepository extends JpaRepository<EpisodeImage, Long> {
    List<EpisodeImage> findByEpisodeIdOrderByOrderAsc(Long episodeId); // episodeId로 정렬된 이미지 조회
}

