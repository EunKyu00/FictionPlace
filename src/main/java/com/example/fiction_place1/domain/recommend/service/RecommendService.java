package com.example.fiction_place1.domain.recommend.service;

import com.example.fiction_place1.domain.board.entity.Board;
import com.example.fiction_place1.domain.board.repository.BoardRepository;
import com.example.fiction_place1.domain.board.service.BoardService;
import com.example.fiction_place1.domain.recommend.entity.Recommend;
import com.example.fiction_place1.domain.recommend.repository.RecommendRepository;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.webtoon.entity.WebToon;
import com.example.fiction_place1.domain.webtoon.repository.WebToonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RecommendRepository recommendRepository;
    private final BoardRepository boardRepository;
    private final WebToonRepository webToonRepository;

    //추천 여부 확인 (SiteUser가 추천했는지)
    public boolean hasSiteUserRecommended(SiteUser siteUser, Board board) {
        return recommendRepository.existsBySiteUserAndBoard(siteUser, board);
    }
    //추천 여부 확인 (CompanyUser가 추천했는지)
    public boolean hasCompanyUserRecommended(CompanyUser companyUser, Board board) {
        return recommendRepository.existsByCompanyUserAndBoard(companyUser, board);
    }
    //추천 추가 (SiteUser가 게시글을 추천하는 경우)
    public void addSiteUserRecommendation(SiteUser siteUser, Board board) {
        Recommend recommend = Recommend.builder()
                .siteUser(siteUser)
                .board(board)
                .build();
        recommendRepository.save(recommend);

        board.setLikes(board.getLikes() + 1);
        boardRepository.save(board);
    }
    //추천 추가 (CompanyUser가 게시글을 추천하는 경우)
    public void addCompanyUserRecommendation(CompanyUser companyUser, Board board) {
        Recommend recommend = Recommend.builder()
                .companyUser(companyUser)
                .board(board)
                .build();
        recommendRepository.save(recommend);

        board.setLikes(board.getLikes() + 1);
        boardRepository.save(board);
    }

    //추천 여부 확인 (SiteUser가 추천했는지)
    public boolean hasSiteUserRecommended(SiteUser siteUser, WebToon webToon) {
        return recommendRepository.existsBySiteUserAndWebToon(siteUser, webToon);
    }
    //추천 여부 확인 (CompanyUser가 추천했는지)
    public boolean hasCompanyUserRecommended(CompanyUser companyUser, WebToon webToon) {
        return recommendRepository.existsByCompanyUserAndWebToon(companyUser, webToon);
    }
    //추천 추가 (SiteUser가 게시글을 추천하는 경우)
    public void addSiteUserRecommendation(SiteUser siteUser, WebToon webToon) {
        Recommend recommend = Recommend.builder()
                .siteUser(siteUser)
                .webToon(webToon)
                .build();
        recommendRepository.save(recommend);

        webToon.setLikes(webToon.getLikes() + 1);
        webToonRepository.save(webToon);
    }
    //추천 추가 (CompanyUser가 게시글을 추천하는 경우)
    public void addCompanyUserRecommendation(CompanyUser companyUser, WebToon webToon) {
        Recommend recommend = Recommend.builder()
                .companyUser(companyUser)
                .webToon(webToon)
                .build();
        recommendRepository.save(recommend);

        webToon.setLikes(webToon.getLikes() + 1);
        webToonRepository.save(webToon);
    }
}

