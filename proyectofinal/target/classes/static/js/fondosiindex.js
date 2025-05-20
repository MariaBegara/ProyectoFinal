

    const fondos = ["img/fondo2.png", "img/fondo3.png", "img/fondo4.png", "img/fondo.png"];
    let index = 0;
    const changeBackground = () => {
      document.body.style.backgroundImage = `url('${fondos[index]}')`;
      index = (index + 1) % fondos.length;
    };
    changeBackground();
    setInterval(changeBackground, 5000); // cambia cada 5 segundos
