import PropTypes from 'prop-types'

function formatDateTime(value) {
  if (!value) return '—'
  return new Date(value).toLocaleString('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

function TripItem({ trip, onDelete, onArchive }) {
  return (
    <li className="trip-item">
      <div className="trip-item__info">
        <h3 className="trip-item__route">
          {trip.departureCity} → {trip.arrivalCity}
        </h3>
        <p>
          <strong>Отправление:</strong> {formatDateTime(trip.departureTime)}
        </p>
        <p>
          <strong>Прибытие:</strong> {formatDateTime(trip.arrivalTime)}
        </p>
        <p>
          <strong>Вокзал:</strong> {trip.stationName}
        </p>
        <p>
          <strong>Статус:</strong> {trip.status === 'active' ? 'активный' : 'архив'}
        </p>
      </div>

      <div className="trip-item__actions">
        <button type="button" className="btn btn--archive" onClick={() => onArchive(trip.id)}>
          Архив
        </button>
        <button type="button" className="btn btn--delete" onClick={() => onDelete(trip.id)}>
          Удалить
        </button>
      </div>
    </li>
  )
}

TripItem.propTypes = {
  trip: PropTypes.shape({
    id: PropTypes.string.isRequired,
    departureCity: PropTypes.string.isRequired,
    arrivalCity: PropTypes.string.isRequired,
    departureTime: PropTypes.string.isRequired,
    arrivalTime: PropTypes.string.isRequired,
    stationName: PropTypes.string.isRequired,
    status: PropTypes.string.isRequired,
  }).isRequired,
  onDelete: PropTypes.func.isRequired,
  onArchive: PropTypes.func.isRequired,
}

export default TripItem
