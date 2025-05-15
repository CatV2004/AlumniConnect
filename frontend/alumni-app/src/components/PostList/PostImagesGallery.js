import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination, Autoplay } from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";

const PostImagesGallery = ({ images }) => {
  if (images.length === 1) {
    return (
      <div className="w-full">
        <img
          src={images[0]}
          alt="post"
          className="w-full h-auto max-h-[500px] object-contain bg-black"
        />
      </div>
    );
  }

  return (
    <Swiper
      modules={[Navigation, Pagination, Autoplay]}
      spaceBetween={0}
      slidesPerView={1}
      navigation
      pagination={{ clickable: true }}
      autoplay={{ delay: 5000 }}
      className="w-full h-auto max-h-[500px] bg-black"
    >
      {images.map((img, index) => (
        <SwiperSlide key={index}>
          <img
            src={img}
            alt={`post-${index}`}
            className="w-full h-auto max-h-[500px] object-contain"
          />
        </SwiperSlide>
      ))}
    </Swiper>
  );
};

export default PostImagesGallery;
