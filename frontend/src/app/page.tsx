'use client';

import { useState } from 'react';

type AnalyzeResponse = {
  riskLevel: string;
  score: number;
  reasons: string[];
  extractedUrl: string | null;
  domain: string | null;
  https: boolean;
};

export default function Home() {
  const [message, setMessage] = useState('');
  const [result, setResult] = useState<AnalyzeResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const analyzeMessage = async () => {
    if (!message.trim()) {
      setError('Enter a Message to analyze...');
      return;
    }

    setError('');
    setResult(null);
    setLoading(true);

    try{
      const response = await fetch('http://localhost:8080/api/analyze', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          message: message,
        }),
      });

      if(!response.ok) {
        throw new Error('Analysis failed.');
      }

      const data: AnalyzeResponse = await response.json();
      setResult(data);
  } catch (error) {
    setError('Unable to analyze the message. Please try again.');
  } finally {
    setLoading(false);
  }
  };

  return (
    <main className='min-h-screen bg-gray-950 text-white'>
      <div className='mx-auto max-w-4xl px-6 py-16'>
        <h1 className='text-4xl font-bold'>De-scam</h1>
        <p className='mt-2 text-gray-400'>Analyze suspicious messages before you act...</p>

        <div className='mt-10'>
          <label htmlFor='message' className='mb-2 block text-sm font-medium'>
            Message:
          </label>
          <textarea
            id='message'
            placeholder='Paste/Enter the message to analyze'
            value={message}
            onChange={(event) => setMessage(event.target.value)}
            className='h-48 w-full resize-none rounded-lg border border-gray-700 bg-gray-900 p-4 text-white outline-none focus:border-gray-500'
          />
          {error && (
            <p className='mt-3 text-sm text-red-400'>
              {error}
            </p>
          )}
          <button
            type='button'
            onClick={analyzeMessage}
            disabled={loading}
            className='mt-4 rounded-lg bg-white px-6 py-3 font-semibold text-black hover:bg-gray-200 disabled:cursor-not-allowed disabled:opacity-50'
          >
           { loading ? 'Analyzing...' : 'Analyze Message' }
          </button>
        </div>

        {result && (
           <div className='mt-10 rounded-lg border border-gray-700 bg-gray-900 p-6'>
             <h2 className='text-xl font-semibold'>Analysis Result</h2>
            <div className='mt-6'>
              <p className='text-sm text-gray-400'>
                Risk Level
              </p>
              <div className={`mt-3 inline-block rounded-full border px-4 py-2 text-sm ont-bold
                ${{
                  LOW: 'border-green-500/30 bg-green-500/10 text-green-400',
                  SUSPICIOUS: 'border-yellow-500/30 bg-yellow-500/10 text-yellow-400',
                  HIGH: 'border-red-500/30 bg-red-500/10 text-red-400',
                  }[result.riskLevel as 'LOW' | 'SUSPICIOUS' | 'HIGH'] ??
                    'border-gray-500/30 bg-gray-500/10 text-gray-400'
                }`}>
                  {result.riskLevel} RISK
              </div>
              
              <p className='mt-2 text-gray-400'>
                Risk Score:{" "}
                <span className='font-semibold text-white'>{result.score}</span>
             </p>
            </div>

            <div className='mt-6'>
              <p className='font-medium'>Why this message is suspicious</p>
              <ul className='mt-3 list-disc space-y-2 pl-5 text-gray-300'>
                {result.reasons.map((reason, index) => (
                  <li key={index}>
                    {reason}
                  </li>
                ))}
              </ul>
            </div>

            {result.extractedUrl &&(
              <div className='mt-6 rounded-lg border border-gray-700 p-4'>
                <p className='font-medium'>
                  URL Information
                </p>
                <div className='mt-4 space-y-3 text-sm'>
                  <div>
                    <p className='text-gray-400'>URL</p>
                    <p className='mt-1 break-all text-gray-200'>
                      {result.extractedUrl}
                    </p>
                  </div>
                  <div>
                    <p className='text-gray-400'>Domain</p>
                    <p className='mt-1 text-gray-200'>
                      {result.domain ?? 'Unable to determine'}
                    </p>
                  </div>
                  <div>
                    <p className='text-gray-400'>HTTPS</p>
                    <p className='mt-1 text-gray-200'>
                      {result.https ? 'Enabled' : 'Not enabled'}
                    </p>
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </main>
  );
}