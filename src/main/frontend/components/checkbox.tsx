export default function Checkbox({
  label,
  id,
  value,
  setValue,
  disabled,
}: {
  label: string;
  id: string;
  value: boolean;
  setValue: (e: boolean) => void;
  disabled?: boolean;
}) {
  return (
    <div className="jenkins-checkbox">
      <input
        type="checkbox"
        id={id}
        name={id}
        checked={value}
        onChange={(e) => setValue(e.target.checked)}
        disabled={disabled}
      />
      <label htmlFor={id}>{label}</label>
    </div>
  );
}
