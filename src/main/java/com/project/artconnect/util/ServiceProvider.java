package com.project.artconnect.util;

import com.project.artconnect.dao.*;

import com.project.artconnect.persistence.*;

import com.project.artconnect.service.*;
import com.project.artconnect.service.impl.*;


/**
 * Service Provider to manage singleton instances of services and handle their
 * initialization.
 */
public class ServiceProvider {
    private static final ArtistDao artistDao = new JdbcArtistDao();
    private static final ArtworkDao artworkDao = new JdbcArtworkDao();
    private static final CommunityMemberDao communityMemberDao = new JdbcCommunityMemberDao();
    private static final ExhibitionDao exhibitionDao = new JdbcExhibitionDao();
    private static final GalleryDao galleryDao = new JdbcGalleryDao();
    private static final WorkshopDao workshopDao = new JdbcWorkshopDao();

    private static final JdbcArtistService artistService = new JdbcArtistService(artistDao);
    private static final JdbcArtworkService artworkService = new JdbcArtworkService(artworkDao);
    private static final JdbcCommunityService communityService = new JdbcCommunityService(communityMemberDao);
    private static final JdbcWorkshopService workshopService = new JdbcWorkshopService(workshopDao);
    private static final JdbcGalleryService galleryService = new JdbcGalleryService(galleryDao);

//    static {
//        // Initialize services with their dependencies
//        artworkService.initData(artistService);
//        galleryService.initData(artworkService);
//        workshopService.initData(artistService);
//        communityService.initData(artworkService);
//    }

    public static ArtistService getArtistService() {
        return artistService;
    }

    public static ArtworkService getArtworkService() {
        return artworkService;
    }

    public static CommunityService getCommunityService() {
        return communityService;
    }

    public static ExhibitionDao getExhibitionDao() {return exhibitionDao;}

    public static GalleryService getGalleryService() {
        return galleryService;
    }

    public static WorkshopService getWorkshopService() {
        return workshopService;
    }
}
