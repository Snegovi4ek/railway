import PropTypes from 'prop-types'
import { useState } from 'react'

const emptyForm = {
  departureCity: '',
  arrivalCity: '',
  departureTime: '',
  arrivalTime: '',
  stationName: '',
}

function TripForm({ onCreate }) {
  const [form, setForm] = useState(emptyForm)

  function handleChange(event) {
    const { name, value } = event.target
    setForm((prevForm) => ({ ...prevForm, [name]: value }))
  }

  function handleSubmit(event) {
    event.preventDefault()

    if (
      !form.departureCity.trim() ||
      !form.arrivalCity.trim() ||
      !form.departureTime ||
      !form.arrivalTime ||
      !form.stationName.trim()
    ) {
      return
    }

    onCreate({
      departureCity: form.departureCity.trim(),
      arrivalCity: form.arrivalCity.trim(),
      departureTime: form.departureTime,
      arrivalTime: form.arrivalTime,
      stationName: form.stationName.trim(),
    })

    setForm(emptyForm)
  }

  return (
    <form className="trip-form" onSubmit={handleSubmit}>
      <label className="trip-form__field">
        Город отправления
        <input
          className="trip-form__input"
          type="text"
          name="departureCity"
          value={form.departureCity}
          onChange={handleChange}
          placeholder="Москва"
        />
      </label>

      <label className="trip-form__field">
        Город прибытия
        <input
          className="trip-form__input"
          type="text"
          name="arrivalCity"
          value={form.arrivalCity}
          onChange={handleChange}
          placeholder="Казань"
        />
      </label>

      <label className="trip-form__field">
        Время отправления
        <input
          className="trip-form__input"
          type="datetime-local"
          name="departureTime"
          value={form.departureTime}
          onChange={handleChange}
        />
      </label>

      <label className="trip-form__field">
        Время прибытия
        <input
          className="trip-form__input"
          type="datetime-local"
          name="arrivalTime"
          value={form.arrivalTime}
          onChange={handleChange}
        />
      </label>

      <label className="trip-form__field">
        Вокзал
        <input
          className="trip-form__input"
          type="text"
          name="stationName"
          value={form.stationName}
          onChange={handleChange}
          placeholder="Казанский вокзал"
        />
      </label>

      <button type="submit" className="btn btn--create">
        Создать
      </button>
    </form>
  )
}

TripForm.propTypes = {
  onCreate: PropTypes.func.isRequired,
}

export default TripForm
